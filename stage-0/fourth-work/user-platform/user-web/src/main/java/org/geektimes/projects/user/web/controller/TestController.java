package org.geektimes.projects.user.web.controller;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.service.UserServiceImpl;
import org.geektimes.web.mvc.controller.PageController;
import org.geektimes.web.mvc.controller.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.geektimes.configuration.microprofile.config.source.servlet.ServletContextConfigInitializer.CONFIG_NAME;

/**
 * 1.完善 my dependency-injection 模块
 * -- 脱离 web.xml 配置实现 ComponentContext 自动初始化
 * -- 使用独立模块并且能够在 user-web 中运行成功
 * 2.完善 my-configuration 模块
 * -- Config 对象如何能被 my-web-mvc 使用
 * -- 可能在 ServletContext 获取
 * -- 如何通过 ThreadLocal 获取
 * 3.去提前阅读 Servlet 规范中 Security 章节（Servlet 容器安全）
 */
@Path("/test")
public class TestController implements RestController {

    public static final Pattern uuid_pattern = Pattern.compile("[^0-9]");

    @Resource(name = "bean/UserService")
    private static UserService userService;

    @GET
    @Path("/component")
    public String component(HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        user.setId(idGenerator());
        user.setName("小马哥");
        user.setPassword("************");
        user.setEmail("mercyblitz@gmail.com");
        user.setPhoneNumber("15011932222");
        System.out.println("userService = " + userService);

        boolean register = userService.register(user);
        if (register) {
            return "register success";
        } else {
            return "register fail";
        }
    }

    public static Long idGenerator() {
        Matcher matcher = uuid_pattern.matcher(UUID.randomUUID().toString());
        String id = matcher.replaceAll("").trim().substring(0, 5);
        Long idLong = Long.valueOf(id);

        while (idLong < 1000) {
            idLong = idGenerator();
        }

        return idLong;
    }

    @GET
//    @POST
    @Path("/config")// application.name
    public Map<String, Object> config(HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> parameterNames = request.getParameterNames();

        Config config = (Config) request.getAttribute(CONFIG_NAME);

        Map<String, Object> map = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String element = parameterNames.nextElement();
//            String parameter = request.getParameter(element);
            String value = config.getValue(element, String.class);
            map.put(element, value);
        }

        return map;
    }
}
