package org.geekbang.spring.cloud.bus.consumer.bus;

import org.springframework.cloud.bus.BusBridge;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-08
 */
@Configuration
public class RedisBusBridge implements BusBridge {

    @Override
    public void send(RemoteApplicationEvent event) {
        System.out.println("event = " + event);
        // TODO do some logical?
    }

}
