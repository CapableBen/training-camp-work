package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.DatabaseUserRepository;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.service.impl.UserServiceImpl;
import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 1.通过自研 Web MVC 框架实现（可以自己实现）一个用户注册，forward 到一个成功的页面（JSP 用法）/register
 * 2.通过 Controller -> Service -> Repository 实现（数据库实现）
 * 3.（非必须）JNDI 的方式获取数据库源（DataSource），在获取 Connection
 */
@Path("/register")
public class RegisterController implements PageController {

    private final UserService userService = new UserServiceImpl();

    @GET
//    @POST
    @Path("/form") // -> register-form.jsp
    public String gotoRegisterForm(HttpServletRequest request, HttpServletResponse response) {
        return "register-form.jsp";
    }

    @GET
//    @POST
    @Path("/submit") // /register -> RegisterController
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");

        User user = new User();
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
}
