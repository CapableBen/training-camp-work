//package com.example.demo.work;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ModifyBeanDefinition implements BeanDefinitionRegistryPostProcessor {
//
//    private static final String HTTPSECURITY_BEAN_NAME =
//            "org.springframework.security.config.annotation.web.configuration.HttpSecurityConfiguration.httpSecurity";
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(HTTPSECURITY_BEAN_NAME);
//        beanDefinition.setScope("singleton");
//        BeanDefinition definition = beanFactory.getBeanDefinition(HTTPSECURITY_BEAN_NAME);
//        System.out.println("definition = " + definition);
//    }
//
//    @Override
//    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//        System.out.println("registry = " + registry);
//        BeanDefinition beanDefinition = registry.getBeanDefinition(HTTPSECURITY_BEAN_NAME);
//        System.out.println("beanDefinition = " + beanDefinition);
//    }
//}
//
