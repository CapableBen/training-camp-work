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
package org.geekbang.springcache.work.cache;

import org.geekbang.springcache.work.domain.Book;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.commands.JedisCommands;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Redis {@link CacheManager} 实现
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 * Date : 2021-04-29
 */
@Configuration
public class RedisCacheManager extends AbstractCacheManager {

    private final JedisPool jedisPool;

    public static final String DEFAULT_CACHE_NAME = "book";

    public RedisCacheManager() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1024);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxWaitMillis(100);
        jedisPoolConfig.setTestOnBorrow(false);
        jedisPoolConfig.setTestOnReturn(true);
        this.jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 2000);
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        // 确保接口不返回 null
        List<Cache> caches = new LinkedList<>();
        prepareCaches(caches);
        return caches;
    }

    protected Cache getMissingCache(String name) {
        Jedis jedis = jedisPool.getResource();
        return new RedisCache(name, jedis);
    }

    private void prepareCaches(List<Cache> caches) {
        Cache bookCache = new RedisCache(DEFAULT_CACHE_NAME, jedisPool.getResource());
        Cache otherCache = new RedisCache("other", jedisPool.getResource());
        caches.add(bookCache);

        initialize(bookCache, otherCache);
    }

    private void initialize(Cache bookCache, Cache otherCache) {
        Book book = new Book();
        book.setId("1");
        book.setName("spring ioc核心编程思想");
        bookCache.put(book.getId(), book);

        book.setId("2");
        book.setName("spring aop核心编程思想");
        bookCache.put(book.getId(), book);

        book.setId("1");
        book.setName("三国演义");
        otherCache.put(book.getId(), book);

        book.setId("2");
        book.setName("红楼梦");
        otherCache.put(book.getId(), book);
    }

}
