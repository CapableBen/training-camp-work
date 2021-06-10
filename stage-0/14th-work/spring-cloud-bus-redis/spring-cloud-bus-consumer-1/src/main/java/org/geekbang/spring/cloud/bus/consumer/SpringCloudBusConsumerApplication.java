package org.geekbang.spring.cloud.bus.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-08
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudBusConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudBusConsumerApplication.class, args);
    }

}
