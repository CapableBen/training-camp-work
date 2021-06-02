package com.geekbang.springcloudconfig.config;

import com.geekbang.springcloudconfig.dynamic.DynamicResourceMessageSource;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.*;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-01
 */
public class MyPropertySourceLocator implements PropertySourceLocator {

    private static final String DEFAULT_LOCATION = "classpath:META-INF/default.properties";

    @Override
    public PropertySource<?> locate(Environment environment) {
        ResourceLoader resourceLoader = new DefaultResourceLoader(getClass().getClassLoader());
        Resource resource = resourceLoader.getResource(DEFAULT_LOCATION);

        Map<String, Object> result = new HashMap<>();
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            Enumeration<?> enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                DynamicResourceMessageSource messageSource = new DynamicResourceMessageSource();
                String message = messageSource.getMessage(key, new Object[]{}, Locale.getDefault());
                result.put(key, message);
            }
        } catch (IOException e) {
            System.out.println("e = " + e);
        }

        return new MapPropertySource("native-file-source", result);
    }

}
