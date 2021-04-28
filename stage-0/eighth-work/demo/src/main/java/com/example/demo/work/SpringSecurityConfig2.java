package com.example.demo.work;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Priority;

@Configuration
@EnableWebSecurity
@Order(20)
public class SpringSecurityConfig2 extends WebSecurityConfigurerAdapter implements PriorityOrdered {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("http = " + http);
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .logout().permitAll()
                .and()
                .formLogin();
        http.csrf();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
//        return LOWEST_PRECEDENCE;
    }
}
