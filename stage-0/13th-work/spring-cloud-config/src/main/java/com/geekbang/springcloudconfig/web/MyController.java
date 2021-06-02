package com.geekbang.springcloudconfig.web;

import com.geekbang.springcloudconfig.dynamic.DynamicResourceMessageSource;
import com.geekbang.springcloudconfig.dynamic.ValueConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.PrintStream;
import java.util.Locale;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-01
 */
@RestController
public class MyController implements EnvironmentAware {

    @Value("${property.from.sample.custom.source}")
    private String name;

    private Environment environment;

    @Resource
    private ValueConfig valueConfig;

    @RequestMapping("/")
    private Object getProperty() {
        System.out.println("name = " + name);
        String property = environment.getProperty("name");

        DynamicResourceMessageSource messageSource = new DynamicResourceMessageSource();
        String message = messageSource.getMessage("name", new Object[]{}, Locale.getDefault());
        return System.out.printf("old value : %s , dynamic value : %s \n", valueConfig.getName(), message);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
