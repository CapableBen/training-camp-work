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
package org.geektimes.cache;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CachePut;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * Configurable {@link CachingProvider}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see Caching#getCachingProvider()
 * @see AbstractCacheManager
 * @since 1.0
 */
public class ConfigurableCachingProvider implements CachingProvider {

    /**
     * The resource name of {@link #getDefaultProperties() the default properties}.
     * javax.cache.CacheManager.mappings.in-memory=org.geektimes.cache.InMemoryCacheManager
     * javax.cache.CacheManager.mappings.redis=org.geektimes.cache.redis.JedisCacheManager
     * javax.cache.CacheManager.mappings.redis=org.geektimes.cache.redis.LettuceCacheManager
     */
    public static final String DEFAULT_PROPERTIES_RESOURCE_NAME = "META-INF/default-caching-provider.properties";

    /**
     * The prefix of property name for the mappings of {@link CacheManager}, e.g:
     * <p>
     * javax.cache.CacheManager.mappings.${uri.scheme}=com.acme.SomeSchemeCacheManager
     */
    public static final String CACHE_MANAGER_MAPPINGS_PROPERTY_PREFIX = "javax.cache.CacheManager.mappings.";

    public static final String DEFAULT_ENCODING = System.getProperty("file.encoding", "UTF-8");

    public static final URI DEFAULT_URI = URI.create("in-memory://localhost/"); // in-memory = DEFAULT_URI.getScheme() = scheme

    private Properties defaultProperties;

    private ConcurrentMap<String, CacheManager> cacheManagersRepository = new ConcurrentHashMap<>();

    @Override
    public ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = Caching.getDefaultClassLoader();
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }
        return classLoader;
    }

    @Override
    public URI getDefaultURI() {
        return DEFAULT_URI;
    }

    @Override
    public Properties getDefaultProperties() {
        if (this.defaultProperties == null) {
            this.defaultProperties = loadDefaultProperties();
        }
        return defaultProperties;
    }

    private Properties loadDefaultProperties() { // 当传入配置为空加载 默认配置文件
        ClassLoader classLoader = getDefaultClassLoader();
        Properties defaultProperties = new Properties();
        try {
            Enumeration<URL> defaultPropertiesResources = classLoader.getResources(DEFAULT_PROPERTIES_RESOURCE_NAME); // 读取配置文件
            while (defaultPropertiesResources.hasMoreElements()) {
                URL defaultPropertiesResource = defaultPropertiesResources.nextElement();
                try (InputStream inputStream = defaultPropertiesResource.openStream();
                     Reader reader = new InputStreamReader(inputStream, DEFAULT_ENCODING)) {
                    defaultProperties.load(reader); // 转换 Properties -> Hashtable.put（public class Properties extends Hashtable<Object,Object>）
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return defaultProperties;
    }

    @Override
    // 通过一系列配置 获取 CacheManager实现类实例 入口
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        URI actualURI = getOrDefault(uri, this::getDefaultURI); // 获取默认 uri
        ClassLoader actualClassLoader = getOrDefault(classLoader, this::getDefaultClassLoader); // 获取默认classLoader
        Properties actualProperties = getOrDefault(properties, this::getDefaultProperties); // 获取默认properties (从配置文件加载到Properties)

        String key = generateCacheManagerKey(actualURI, actualClassLoader, actualProperties); // stringBuilder 创建 key

        return cacheManagersRepository.computeIfAbsent(key, k ->
                newCacheManager(actualURI, actualClassLoader, actualProperties)); // computeIfAbsent 判断不存在添加
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return getCacheManager(uri, classLoader, getDefaultProperties());
    }

    @Override
    public CacheManager getCacheManager() {
        return getCacheManager(getDefaultURI(), getDefaultClassLoader(), getDefaultProperties());
    }

    @Override
    public void close() {
        this.close(this.getDefaultURI(), this.getDefaultClassLoader());
    }

    @Override
    public void close(ClassLoader classLoader) {
        this.close(this.getDefaultURI(), classLoader);
    }

    @Override
    public void close(URI uri, ClassLoader classLoader) {
        for (CacheManager cacheManager : cacheManagersRepository.values()) {
            if (Objects.equals(cacheManager.getURI(), uri)
                    && Objects.equals(cacheManager.getClassLoader(), cacheManager)) {
                cacheManager.close();
            }
        }
    }

    @Override
    public boolean isSupported(OptionalFeature optionalFeature) {
        return false;
    }

    private String generateCacheManagerKey(URI uri, ClassLoader classLoader, Properties properties) {
        StringBuilder keyBuilder = new StringBuilder(uri.toASCIIString())
                .append("-").append(classLoader)
                .append("-").append(properties);
        return keyBuilder.toString();
    }


    private <T> T getOrDefault(T value, Supplier<T> defaultValue) {
        return value == null ? defaultValue.get() : value;
    }

    private static String getCacheManagerClassNamePropertyName(URI uri) { // javax.cache.CacheManager.mappings. + InMemoryCacheManager
        String scheme = uri.getScheme();
        return CACHE_MANAGER_MAPPINGS_PROPERTY_PREFIX + scheme;
    }

    private String getCacheManagerClassName(URI uri, Properties properties) {
        String propertyName = getCacheManagerClassNamePropertyName(uri); // 组装传入uri
        String className = properties.getProperty(propertyName); // 从加载的properties中匹配，匹配传入uri
        if (className == null) {
            throw new IllegalStateException(format("The implementation class name of %s that is the value of property '%s' " +
                    "must be configured in the Properties[%s]", CacheManager.class.getName(), propertyName, properties));
        }
        return className;
    }

    private Class<? extends AbstractCacheManager> getCacheManagerClass(URI uri, ClassLoader classLoader, Properties properties)
            throws ClassNotFoundException, ClassCastException {

        String className = getCacheManagerClassName(uri, properties);
        Class<? extends AbstractCacheManager> cacheManagerImplClass = null;
        Class<?> cacheManagerClass = classLoader.loadClass(className); // 根据从property匹配的类路径 通过classLoader进行加载
        // The AbstractCacheManager class must be extended by the implementation class,
        // because the constructor of the implementation class must have four arguments in order:
        // [0] - CachingProvider
        // [1] - URI
        // [2] - ClassLoader
        // [3] - Properties
        if (!AbstractCacheManager.class.isAssignableFrom(cacheManagerClass)) { // 判断必须是AbstractCacheManager子类（该类为抽象类，方法签名必须满足要求）
            throw new ClassCastException(format("The implementation class of %s must extend %s",
                    CacheManager.class.getName(), AbstractCacheManager.class.getName()));
        }

        cacheManagerImplClass = (Class<? extends AbstractCacheManager>) cacheManagerClass; // 转换返回

        return cacheManagerImplClass;
    }

    // 创建 CacheManager 方法入口
    private CacheManager newCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        CacheManager cacheManager = null;
        try {
            // 通过一系列操作 -> 根据传入uri 返回配置文件中匹配的类对象（cacheManagerClass）
            Class<? extends AbstractCacheManager> cacheManagerClass = getCacheManagerClass(uri, classLoader, properties);
            Class[] parameterTypes = new Class[]{CachingProvider.class, URI.class, ClassLoader.class, Properties.class}; // 根据参数转换类型数组
            Constructor<? extends AbstractCacheManager> constructor = cacheManagerClass.getConstructor(parameterTypes); // 根据 类 和 参数类 数组获取构造方法
            cacheManager = constructor.newInstance(this, uri, classLoader, properties); // 根据 构造类 和 传入参数，通过反射生成实例 返回
        } catch (Throwable e) {
            throw new CacheException(e);
        }
        return cacheManager;
    }
}
