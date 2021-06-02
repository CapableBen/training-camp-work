package org.geektimes.cache.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.geektimes.cache.AbstractCache;
import org.geektimes.cache.ExpirableEntry;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LettuceCache<K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {

    private final RedisClient redisClient;
    private final StatefulRedisConnection<K, V> connect;
    private final RedisCommands<K, V> redisCommands;

    protected LettuceCache(CacheManager cacheManager, String cacheName, Configuration<K, V> configuration, RedisClient redisClient) {
        super(cacheManager, cacheName, configuration);
        this.redisClient = redisClient;
        this.connect = redisClient.connect(new GenericRedisCodec<>());
        this.redisCommands = this.connect.sync(); //TODO 异步 响应式 等
    }

    @Override
    protected boolean containsEntry(K key) throws CacheException, ClassCastException {
        Long exists = redisCommands.exists(key);
        return exists > 0;
    }

    @Override
    protected ExpirableEntry<K, V> getEntry(K key) throws CacheException, ClassCastException {
        V value = redisCommands.get(key);
        return ExpirableEntry.of(key, value);
    }

    @Override
    protected void putEntry(ExpirableEntry<K, V> newEntry) throws CacheException, ClassCastException {
        String result = redisCommands.set(newEntry.getKey(), newEntry.getValue());
    }

    @Override
    protected ExpirableEntry<K, V> removeEntry(K key) throws CacheException, ClassCastException {
        // sync.getdel(key); // io.lettuce.core.RedisCommandExecutionException: ERR unknown command 'GETDEL'
        V value = redisCommands.get(key);
        Long del = redisCommands.del(key);
        return del > 0 ? ExpirableEntry.of(key, value) : null;
    }

    @Override
    protected void clearEntries() throws CacheException {
    }

    @Override
    protected Set<K> keySet() {
        Set<K> keys = (Set<K>) redisCommands.keys(null);
        return keys;
    }

    @Override
    protected void doClose() {
        connect.close();
        redisClient.shutdown();
    }
}
