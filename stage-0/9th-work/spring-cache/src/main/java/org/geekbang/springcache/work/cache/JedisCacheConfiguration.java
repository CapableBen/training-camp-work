package org.geekbang.springcache.work.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * configure JedisPool {@link CachingConfigurerSupport}
 *
 * @author: Ben
 * @since: 1.0.0
 * @date: 2021-05-10
 */
@Configuration
public class JedisCacheConfiguration extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);

//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxTotal(1024);
//        jedisPoolConfig.setMaxIdle(100);
//        jedisPoolConfig.setMaxWaitMillis(100);
//        jedisPoolConfig.setTestOnBorrow(false);
//        jedisPoolConfig.setTestOnReturn(true);
//       this.jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 2000);

        return jedisPool;
    }

}