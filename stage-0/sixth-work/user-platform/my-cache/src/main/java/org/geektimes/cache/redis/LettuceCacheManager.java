package org.geektimes.cache.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.geektimes.cache.AbstractCacheManager;

import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

public class LettuceCacheManager extends AbstractCacheManager {

    private final RedisClient redisClient;

    public LettuceCacheManager(CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
        super(cachingProvider, uri, classLoader, properties);
        this.redisClient = RedisClient.create(RedisURI.create(uri));
    }

    @Override
    protected <K, V, C extends Configuration<K, V>> LettuceCache doCreateCache(String cacheName, C configuration) {
        return new LettuceCache(this, cacheName, configuration, redisClient);
    }

    @Override
    protected void doClose() {
        redisClient.shutdown();
    }
}
