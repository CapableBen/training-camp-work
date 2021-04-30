package com.example.demo.work;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.Map;

@Configuration
public class WebSecurityConfigurerAdapterLimit implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        lookupCollectionByType(applicationContext, WebSecurityConfigurerAdapter.class);
    }

    private static void lookupCollectionByType(BeanFactory beanFactory, Class<? extends WebSecurityConfigurerAdapter> klass) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, ? extends WebSecurityConfigurerAdapter> beansOfType = listableBeanFactory.getBeansOfType(klass);
            System.out.printf("查找到的所有的 [%s] 集合对象：%s %n", klass, beansOfType);
        }
    }
}
