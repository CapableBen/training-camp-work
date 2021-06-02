package org.geektimes.rest.demo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class RestClientDemo {

    public static void main(String[] args) {
        testGet();
        testPost();
    }

    private static void testGet() {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target("http://127.0.0.1:8080/hello/world")      // WebTarget
                .request() // Invocation.Builder
                .get();                                     //  Response

        String content = response.readEntity(String.class);

        System.out.println("post: " + content);
    }

    private static void testPost() {
        Map<String, Object> map = new HashMap<>();
//        map.put("id", "abc");
        map.put("id", "1");
        map.put("name", "小马哥");
        map.put("password", "******");
        map.put("email", "mercyblitz@gmail.com");
        map.put("phoneNumber", "abcdefg");

        Client client = ClientBuilder.newClient();
        Response response = client
                .target("http://127.0.0.1:8080/test/rest")
                .request()
                .post(Entity.json(map));

        String content = response.readEntity(String.class);

        System.out.println("post: " + content);
    }
}
