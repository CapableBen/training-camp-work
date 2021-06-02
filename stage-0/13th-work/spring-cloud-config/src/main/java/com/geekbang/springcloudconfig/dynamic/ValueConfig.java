package com.geekbang.springcloudconfig.dynamic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-01
 */
@RefreshScope
@Component
public class ValueConfig {

    @Value("${name}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
