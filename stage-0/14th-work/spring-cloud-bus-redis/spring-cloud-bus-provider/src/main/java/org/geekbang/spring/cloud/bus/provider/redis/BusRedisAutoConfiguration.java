package org.geekbang.spring.cloud.bus.provider.redis;

import org.geekbang.spring.cloud.bus.provider.busbridge.RedisBusBridge;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.autoconfigure.LifecycleMvcEndpointAutoConfiguration;
import org.springframework.cloud.bus.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-09
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBusEnabled
@ConditionalOnClass({RedisBusBridge.class, RedisConfig.class})
@EnableConfigurationProperties(BusProperties.class)
//@AutoConfigureBefore({ BindingServiceConfiguration.class, BusAutoConfiguration.class })
// so stream bindings work properly
@AutoConfigureAfter({LifecycleMvcEndpointAutoConfiguration.class, PathServiceMatcherAutoConfiguration.class})
public class BusRedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(BusBridge.class)
    public RedisBusBridge redisBusBridge(RedisService redisService, BusProperties properties) {
        return new RedisBusBridge(redisService, properties);
    }

}
