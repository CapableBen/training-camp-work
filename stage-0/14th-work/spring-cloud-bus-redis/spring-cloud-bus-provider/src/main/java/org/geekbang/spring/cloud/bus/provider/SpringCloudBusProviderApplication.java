package org.geekbang.spring.cloud.bus.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-08
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudBusProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudBusProviderApplication.class, args);
    }

}
