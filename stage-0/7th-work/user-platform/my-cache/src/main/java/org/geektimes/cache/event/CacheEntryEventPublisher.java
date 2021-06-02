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
import javax.cache.event.CacheEntryEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * The Publisher of {@link javax.cache.event.CacheEntryEvent}
 *
 * 缓存对象 事件发布器
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0
 */
public class CacheEntryEventPublisher {

    private List<ConditionalCacheEntryEventListener> listeners = new LinkedList<>();

    // 添加 监听器
    public void registerCacheEntryListener(CacheEntryListenerConfiguration configuration) {
        // 根据传入 事件监听配置类（cacheEntryListenerConfiguration）
        // 实例化事件监听适配器（CacheEntryEventListenerAdapter）
        // 保存在 缓存对象 事件发布器（LinkedList有序）
        CacheEntryEventListenerAdapter listenerAdapter = new CacheEntryEventListenerAdapter(configuration);
        listeners.add(listenerAdapter);
    }

    // 删除 监听器
    public void deregisterCacheEntryListener(CacheEntryListenerConfiguration configuration) {
        CacheEntryEventListenerAdapter listenerAdapter = new CacheEntryEventListenerAdapter(configuration);
        listeners.remove(listenerAdapter);
    }

    // 广播
    public <K, V> void publish(CacheEntryEvent<? extends K, ? extends V> event) {
        // 对保存在发布器中的 监听器 全部执行onEvent方法，参数为eventObject
        // 实际上 执行 ConditionalCacheEntryEventListener 接口的实现类 CacheEntryEventListenerAdapter 的 onEvent 方法
        listeners.forEach(listener -> listener.onEvent(event));
    }

}
