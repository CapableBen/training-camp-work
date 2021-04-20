/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.geektimes.cache.event;

import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Factory;
import javax.cache.event.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableMap;

/**
 * The adapter of {@link ConditionalCacheEntryEventListener} based on {@link CacheEntryListenerConfiguration}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see CacheEntryListenerConfiguration
 * @since 1.0
 */
public class CacheEntryEventListenerAdapter<K, V> implements ConditionalCacheEntryEventListener<K, V> {

    private static List<Object> eventTypesAndHandleMethodNames = asList(
            EventType.CREATED, "onCreated",
            EventType.UPDATED, "onUpdated",
            EventType.EXPIRED, "onExpired",
            EventType.REMOVED, "onRemoved"
    );

    /*
     * Configuration 作为 Listener 的包装类（具有以下功能）：
     * 1.Factory<CacheEntryListener<? super K, ? super V>> getCacheEntryListenerFactory(); 获取监听器实例
     * 2.boolean isOldValueRequired(); 是否需要老值
     * 3.Factory<CacheEntryEventFilter<? super K, ? super V>> getCacheEntryEventFilterFactory(); 获取过滤器实例
     * 4.boolean isSynchronous(); 是否同步
     * 创建Adapter实例时，需要配置 CacheEntryListenerConfiguration 的实现类例如（TestCacheEntryListener类）
     */
    private final CacheEntryListenerConfiguration<K, V> configuration;

    private final CacheEntryEventFilter<? super K, ? super V> cacheEntryEventFilter;

    private final CacheEntryListener<? super K, ? super V> cacheEntryListener;

    private final Map<EventType, Method> eventTypeMethods;// 匹配之后的 监听类型 和 方法映射。实际上为TestCacheEntryListener中的（onCreated，onUpdated，onExpired，onRemoved）

    private final Executor executor;

    public CacheEntryEventListenerAdapter(CacheEntryListenerConfiguration<K, V> configuration) {
        this.configuration = configuration;
        this.cacheEntryEventFilter = getCacheEntryEventFilter(configuration);
        this.cacheEntryListener = configuration.getCacheEntryListenerFactory().create(); // Factory#create返回CacheEntryListener实例
        this.eventTypeMethods = determineEventTypeMethods(cacheEntryListener);
        this.executor = getExecutor(configuration);
    }

    @Override
    public boolean supports(CacheEntryEvent<? extends K, ? extends V> event) {
        return supportsEventType(event) && cacheEntryEventFilter.evaluate(event); // 根据 传入的cacheEntryEventFilter#evaluate()评估是否拦截（TestCacheEntryListener）
    }

    private boolean supportsEventType(CacheEntryEvent<? extends K, ? extends V> event) { // 判断监听器类型是否支持
        return getSupportedEventTypes().contains(event.getEventType());
    }

    @Override
    // CacheEntryEvent<? extends K, ? extends V> event 实际实现类为当前 GenericCacheEntryEvent 类
    public void onEvent(CacheEntryEvent<? extends K, ? extends V> event) { // 事件监听方法触发入口
        if (!supports(event)) { // 执行之前 先评估
            return;
        }

        EventType eventType = event.getEventType();
        Method handleMethod = eventTypeMethods.get(eventType);//获取 根据构造方法初始化时， 已经匹配监听器映射方法

        executor.execute(() -> { //获取 根据构造方法初始化时， 已经获取的executor对象，进行执行
            try {
                handleMethod.invoke(cacheEntryListener, singleton(event)); // 反射执行 eventListener(TestCacheEntryListener)中的处理事件源的方法
                // onRemoved(Iterable<CacheEntryEvent<? extends K, ? extends V>> cacheEntryEvents)
                // singleton(event) 目的为了匹配 Iterable<CacheEntryEvent<>>，目前使用单例
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new CacheEntryListenerException(e);
            }
        });
    }

    @Override
    public Set<EventType> getSupportedEventTypes() {
        return eventTypeMethods.keySet();
    }

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public int hashCode() {
        return configuration.hashCode();
    }

    // 重写 equals方法
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CacheEntryEventListenerAdapter)) {
            return false;
        }
        CacheEntryEventListenerAdapter another = (CacheEntryEventListenerAdapter) object;
        return this.configuration.equals(another.configuration);
    }

    // Factory#create()方法获取CacheEntryEventFilter实例
    // filter为空等价于不拦截 == true
    private CacheEntryEventFilter<? super K, ? super V> getCacheEntryEventFilter(CacheEntryListenerConfiguration<K, V> configuration) {
        Factory<CacheEntryEventFilter<? super K, ? super V>> factory = configuration.getCacheEntryEventFilterFactory();
        CacheEntryEventFilter<? super K, ? super V> filter = null;

        if (factory != null) {
            filter = factory.create();
        }

        if (filter == null) {
            // When null no filtering is applied and all appropriate events are notified.
            filter = e -> true;
        }

        return filter;
    }

    // 根据传入CacheEntryListener类型 获取事件类型方法映射（unmodifiableMap不可变）
    private Map<EventType, Method> determineEventTypeMethods(CacheEntryListener<? super K, ? super V> cacheEntryListener) {
        Map<EventType, Method> eventTypeMethods = new HashMap<>(EventType.values().length);
        Class<?> cacheEntryListenerClass = cacheEntryListener.getClass();
        for (int i = 0; i < eventTypesAndHandleMethodNames.size(); ) { //总数8，会循环4次，原因i++2次，实际上相当于2个一组循环一次
            EventType eventType = (EventType) eventTypesAndHandleMethodNames.get(i++);
            String handleMethodName = (String) eventTypesAndHandleMethodNames.get(i++);
            try {
                // 通过反射 获取cacheEntryListenerClass类中，匹配集合中定义好的 handleMethodName 的方法
                Method handleMethod = cacheEntryListenerClass.getMethod(handleMethodName, Iterable.class);// 根据已经自定义方法，匹配监听器该方法是否存在
                if (handleMethod != null) {
                    eventTypeMethods.put(eventType, handleMethod);
                }
            } catch (NoSuchMethodException ignored) {
            }

        }
        return unmodifiableMap(eventTypeMethods);
    }

    private Object[] determineEventTypeAndHandleMethod(CacheEntryListener<? super K, ? super V> cacheEntryListener) {
        Class<?> cacheEntryListenerClass = cacheEntryListener.getClass();
        Object[] eventTypeAndHandleMethod = new Object[2];
        for (int i = 0; i < eventTypesAndHandleMethodNames.size(); ) {
            EventType eventType = (EventType) eventTypesAndHandleMethodNames.get(i++);
            String handleMethodName = (String) eventTypesAndHandleMethodNames.get(i++);
            try {
                Method handleMethod = cacheEntryListenerClass.getMethod(handleMethodName, Iterable.class);
                if (handleMethod != null) {
                    eventTypeAndHandleMethod[0] = eventType;
                    eventTypeAndHandleMethod[1] = handleMethod;
                }
            } catch (NoSuchMethodException ignored) {
            }

        }
        return eventTypeAndHandleMethod;
    }

    // configuration.isSynchronous() == boolean -> 转换为 Executor可执行对象
    // true： Runnable::run 主线程（同步）
    // false： ForkJoinPool.commonPool() 开启子线程执行（异步）
    private Executor getExecutor(CacheEntryListenerConfiguration<K, V> configuration) {
        Executor executor = null;
        if (configuration.isSynchronous()) {
            executor = Runnable::run;
        } else {
            executor = ForkJoinPool.commonPool();
        }
        return executor;
    }
}
