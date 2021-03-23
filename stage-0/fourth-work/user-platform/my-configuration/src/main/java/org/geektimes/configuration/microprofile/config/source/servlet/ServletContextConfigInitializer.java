package org.geektimes.configuration.microprofile.config.source.servlet;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 如何注册当前 ServletContextListener 实现
 *
 * @see ServletConfigInitializer
 */
@WebListener
public class ServletContextConfigInitializer implements ServletContextListener {

    public final static String CONFIG_NAME = "servletContextInitParameters";

    public static ThreadLocal<ServletContext> threadLocalServletContext = new ThreadLocal<>();

    public static ThreadLocal<Config> threadLocalConfig = new ThreadLocal<>();

    public static ClassLoader classLoader;

    public static Config getConfig() {
        return threadLocalConfig.get();
    }

    public static void initConfig(Config config) {
        if (threadLocalConfig.get() == null) {
            threadLocalConfig.set(config);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        threadLocalServletContext.set(servletContext);
        // 获取当前 ClassLoader
        // ClassLoader classLoader = servletContext.getClassLoader();
        try {
            if (classLoader == null) {
                classLoader = servletContext.getClassLoader();
            }
        } catch (Exception cause) {
//            throw new IllegalStateException("获取classLoader发生错误", cause);
            System.out.println("获取classLoader发生错误 = " + cause.getMessage());
        }

        ServletContextConfigSource servletContextConfigSource = new ServletContextConfigSource(servletContext);
        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
        ConfigBuilder configBuilder = configProviderResolver.getBuilder();
        // 配置 ClassLoader
        configBuilder.forClassLoader(classLoader);
        // 默认配置源（内建的，静态的）
        configBuilder.addDefaultSources();
        // 通过发现配置源（动态的）
        configBuilder.addDiscoveredConverters();
        // 增加扩展配置源（基于 Servlet 引擎）
        configBuilder.withSources(servletContextConfigSource);
        // 获取 Config
        Config config = configBuilder.build();
        // 注册 Config 关联到当前 ClassLoader
        configProviderResolver.registerConfig(config, classLoader);

//        initConfig(config);
        servletContext.setAttribute(CONFIG_NAME, config);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
//        ServletContext servletContext = servletContextEvent.getServletContext();
//        ClassLoader classLoader = servletContext.getClassLoader();
//        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
//        Config config = configProviderResolver.getConfig(classLoader);
    }
}
