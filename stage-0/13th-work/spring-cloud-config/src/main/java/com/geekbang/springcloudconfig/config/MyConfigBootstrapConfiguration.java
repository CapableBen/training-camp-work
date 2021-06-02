package com.geekbang.springcloudconfig.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-01
 */
@Configuration
public class MyConfigBootstrapConfiguration {

    @Bean
    public MyPropertySourceLocator setMyPropertySourceLocator() {
        MyPropertySourceLocator myPropertySourceLocator = new MyPropertySourceLocator();

        return myPropertySourceLocator;
    }

}
