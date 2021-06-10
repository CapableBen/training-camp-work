package org.geekbang.spring.cloud.bus.provider.web;

import org.apache.commons.lang.StringUtils;
import org.geekbang.spring.cloud.bus.provider.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.bus.event.Destination;
import org.springframework.cloud.bus.event.EnvironmentChangeRemoteApplicationEvent;
import org.springframework.cloud.bus.event.PathDestinationFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-09
 */
@RestController
@EnableScheduling
public class MessageSenderController {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${spring.cloud.bus.id}")
    private String originService;

    @Value("${spring.cloud.bus.destination}")
    private String destinationName;

    @Autowired
    private PathDestinationFactory pathDestinationFactory;

    // 自定义模拟 http://localhost:9990/actuator/busenv?name=name&value=xiaomage&destination=bus-consumer-1
    @RequestMapping("/")
    public void sendMessage0(@RequestParam String id, @RequestParam String name, @RequestParam String destination) {
        destination = StringUtils.isNotEmpty(destination) ? destination : destinationName;

        EnvironmentChangeRemoteApplicationEvent event = new EnvironmentChangeRemoteApplicationEvent(
                this, originService, getDestination(destination), getUserMap(id, name));

//        CustomEvent customEvent = new CustomEvent(this, getUser(), originService, destination);

        applicationContext.publishEvent(event);

        System.out.println("send ok!");
    }

    private Destination getDestination(String destination) {
        Assert.notNull(destination, "destination must not be null");
        return () -> destination;
    }

    private Map<String, String> getUserMap(String id, String name) {
        Map<String, String> values = new HashMap<>();
        values.put("id", id != null ? id : "1");
        values.put("name", name != null ? name : "xxx");
        return values;
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setName("xxx");
        return user;
    }

}
