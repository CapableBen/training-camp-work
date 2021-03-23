package org.geektimes.context.servlet;

import org.geektimes.context.ComponentContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 如何注册当前 ServletContextListener 实现
 *
 * @see ServletComponentInitializer
 */
@WebListener
public class ServletContextComponentInitializer implements ServletContextListener {

    public static ClassLoader classLoader;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        try { // TODO 进入两次？
            if (classLoader == null) {
                classLoader = servletContext.getClassLoader();
            }
        } catch (Exception e) {
            System.out.println("servletContext获取classLoader出现异常 = " + e.getMessage());
        }
        ComponentContext context = new ComponentContext();
        context.init(servletContext, classLoader);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ComponentContext context = ComponentContext.getInstance();
        context.destroy();
    }
}