package org.geektimes.projects.user.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.util.*;

@Path("/oauth")
public class GitHubCallBackController implements RestController {

    public static final String CLIENT_ID = "c7a8bdd2319ec71108fd"; // TODO 替换
    public static final String CLIENT_SECRET = "fa7fcab4b8d9cffcf176cacf7b355ab8ea0044f6";  // TODO 替换
    public static final String CALLBACK = "http://localhost:8080/oauth/callback";  // TODO 替换

    // 获取code
    public static final String CODE_URL = "https://github.com/login/oauth/authorize?client_id=CLIENT_ID&state=STATE&redirect_uri=CALLBACK";
    // 获取token
    public static final String TOKEN_URL = "https://github.com/login/oauth/access_token?client_id=CLIENT_ID&client_secret=CLIENT_SECRET&code=CODE&redirect_uri=CALLBACK";
    // 获取用户信息
    public static final String USER_INFO_URL = "https://api.github.com/user";

    @Path("/callback")
    public Map<String, String> callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        System.out.printf("code: %s, state: %s\n", code, state);

        if (code == null || code.equals("") || state == null || state.equals("")) {
            throw new RuntimeException("code and state must not be null!");
        }

        return this.callback(code, state);
    }

    public Map<String, String> callback(String code, String state) throws Exception {
        Map<String, String> responseMap = new HashMap<>();

        if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(state)) {
            String token_url = TOKEN_URL.replace("CLIENT_ID", CLIENT_ID)
                    .replace("CLIENT_SECRET", CLIENT_SECRET)
                    .replace("CALLBACK", CALLBACK)
                    .replace("CODE", code);
            String responseStr = doGet(token_url, null); // 获取token
            String access_token = getQueryParams(responseStr, "access_token");
            System.out.println("access_token = " + access_token);

            responseStr = doGet(USER_INFO_URL, access_token);// 获取个人信息

            responseMap = JSONString2Map(responseStr);
        }

        return responseMap;
    }

    public static String doGet(String url, String token) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(50000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(50000)
                .build();
        httpGet.setConfig(requestConfig);

        if (token != null) {
            httpGet.setHeader("Authorization", "token " + token);
        }

        CloseableHttpResponse response = httpclient.execute(httpGet); //发送一个http请求

        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity responseEntity = response.getEntity(); //获取响应的内容
            return EntityUtils.toString(responseEntity);
        }

        return null;
    }

    public static String getQueryParams(String url, String name) {
        Map<String, String> params = new HashMap<>();
        for (String param : url.split("&")) {
            String[] pair = param.split("=");
            String key = pair[0];
            String value = "";
            if (pair.length > 1) {
                value = pair[1];
            }
            String finalValue = value;
            params.computeIfAbsent(key, k -> params.put(k, finalValue));
        }

        return params.get(name);
    }

    public static Map<String, String> JSONString2Map(String jsonString) {
        Map<String, String> map = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            map.put(key, value);
        }
        return map;
    }
}