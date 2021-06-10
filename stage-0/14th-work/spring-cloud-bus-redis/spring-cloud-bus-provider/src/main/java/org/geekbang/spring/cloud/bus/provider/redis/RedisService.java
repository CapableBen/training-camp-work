package org.geekbang.spring.cloud.bus.provider.redis;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-09
 */
public interface RedisService {

    /**
     * 数据发布
     *
     * @param channel
     * @param message
     * @return
     */
    void convertAndSend(String channel, Object message);
}
