package org.geektimes.projects.user.web.controller;

import org.geektimes.configuration.microprofile.config.JavaConfig;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.*;

/**
 * 获取 配置信息 Controller
 * 需求一（必须）
 *    整合 https://jolokia.org/
 *    实现一个自定义 JMX MBean，通过 Jolokia 做 Servlet 代理
 * 需求二（选做）
 *    继续完成 Microprofile config API 中的实现
 *    扩展 org.eclipse.microprofile.config.spi.ConfigSource 实现，包括 OS 环境变量，以及本地配置文件
 *    扩展 org.eclipse.microprofile.config.spi.Converter 实现，提供 String 类型到简单类型
 *    通过 org.eclipse.microprofile.config.Config 读取当前应用名称
 *    应用名称 property name = “application.name”
 */
@Path("/config")
public class ConfigController implements RestController {

    private static final Set<Class<?>> classSet = new HashSet<>();

    static {
        classSet.add(Double.class);
        classSet.add(Integer.class);
        classSet.add(Long.class);
        classSet.add(Float.class);
        classSet.add(Boolean.class);
    }

    @GET
//    @POST
    @Path("/test")
    public Map<String, Object> test(HttpServletRequest request, HttpServletResponse response) {
        JavaConfig javaConfig = new JavaConfig();

        Map<String, Object> map = new HashMap<>();

        String name = javaConfig.getValue("application.name", String.class);
        Integer version = javaConfig.getValue("application.version", Integer.class);
        map.put("application.name", name);
        map.put("application.version", version);

        return map;
    }

    @GET
//    @POST
    @Path("/get")
    public Map<String, Object> config(HttpServletRequest request, HttpServletResponse response) {
        JavaConfig javaConfig = new JavaConfig();

        Enumeration<String> parameterNames = request.getParameterNames();

        Map<String, Object> map = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String element = parameterNames.nextElement();
            String parameter = request.getParameter(element);
            Object value;
            try {
                value = javaConfig.getValue(element, getTypeClass(parameter));
            } catch (Exception e) {
                value = javaConfig.getStringValue(element);
            }
            map.put(element, value);
        }

        return map;
    }

    private Class<?> getTypeClass(String parameter) {
        Class<?> clazz = classSet.stream()
                .filter(klass -> parameter != null
                        && !parameter.equals("")
                        && klass.getSimpleName().equalsIgnoreCase(parameter))
                .findFirst().orElse(String.class);
        return clazz;
    }

}
