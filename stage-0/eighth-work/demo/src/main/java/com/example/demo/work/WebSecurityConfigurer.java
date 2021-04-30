package com.example.demo.work;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

public interface WebSecurityConfigurer {

    void configure(HttpSecurity builder) throws Exception;

    void configure(WebSecurity builder) throws Exception;

}
