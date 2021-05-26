package com.geekbang.springmybatis.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-05-12
 */
@ConfigurationProperties(prefix = "spring.mybatis")
public class MybatisProperties {

    //TODO 可以通过 Properties 进行参数转换/判断/提示等操作

    private String driver;

    private String url;

    private String username;

    private String password;

    private Resource configLocation;

    private Resource mapperLocations;

    private String environment;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Resource getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    public Resource getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(Resource mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        return "MybatisProperties{" +
                "driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", configLocation=" + configLocation +
                ", mapperLocations=" + mapperLocations +
                '}';
    }
}
