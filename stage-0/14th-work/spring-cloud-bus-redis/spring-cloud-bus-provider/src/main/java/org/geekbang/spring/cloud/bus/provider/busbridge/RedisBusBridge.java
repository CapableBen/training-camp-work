package org.geekbang.spring.cloud.bus.provider.busbridge;

import org.geekbang.spring.cloud.bus.provider.redis.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.bus.BusBridge;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-08
 */
public class RedisBusBridge implements BusBridge {

    @Value("${topic.name}")
    private String topicName;

    private final BusProperties properties;

    private final RedisService redisService;

    public RedisBusBridge(RedisService redisService, BusProperties properties) {
        this.properties = properties;
        this.redisService = redisService;
    }

    @Override
    public void send(RemoteApplicationEvent event) {
        System.out.println("event = " + event);
        System.out.println("properties = " + properties);
        redisService.convertAndSend(topicName, event);
    }

}
