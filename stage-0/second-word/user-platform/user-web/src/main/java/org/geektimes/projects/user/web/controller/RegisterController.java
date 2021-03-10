package org.geektimes.projects.user.web.controller;

import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.validator.bean.validation.ValidatorUtils;
import org.geektimes.web.mvc.controller.PageController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.geektimes.projects.user.validator.bean.validation.ValidatorUtils.idGenerator;

/**
 * 1.通过课堂上的简易版依赖注入和依赖查找，实现用户注册功能
 * 2.通过 UserService 实现用户注册注册用户需要校验
 * 3.Id：必须大于 0 的整数
 * 4.密码：6-32 位 电话号码: 采用中国大陆方式（11 位校验）
 */
@Path("/register")
public class RegisterController implements PageController {

    private final UserService userService = ComponentContext.getInstance().getComponent("bean/UserService");

    @GET
//    @POST
    @Path("/form")
    public String gotoRegisterForm(HttpServletRequest request, HttpServletResponse response) {
        return "register-form.jsp";
    }

    @GET
//    @POST
    @Path("/submit")
    public String register(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        // TODO validate

        User user = new User();
        user.setId(idGenerator());
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        System.out.println("user = " + user);

        boolean register = userService.register(user);
        if (register) {
            return "register-success.jsp";
        } else {
            return "index.jsp";
        }
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
