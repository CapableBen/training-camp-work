package org.geektimes.projects.user.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geektimes.projects.user.domain.User;
import org.geektimes.web.mvc.controller.RestController;
import org.hibernate.criterion.AggregateProjection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;

@Path("/test")
public class MyRestController implements RestController {

    // @GET
    @POST
    @Path("/rest")
    public User execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new InputStreamReader(request.getInputStream(), "UTF-8"), User.class);
    }
}
