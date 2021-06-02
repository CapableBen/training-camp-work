package org.geektimes.projects.user.web.controller;

import org.geektimes.web.mvc.controller.PageController;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

//  使用 Spring Boot 来实现一个整合 Gitee 或者 Github OAuth2 认证
//        Servlet
@Path("/oauth")
public class GitHubOAuth2Controller implements PageController {

    @GET
    @Path("/login")
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "login.jsp";
    }

}
