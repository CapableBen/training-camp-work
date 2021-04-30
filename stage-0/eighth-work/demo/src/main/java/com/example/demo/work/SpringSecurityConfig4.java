package com.example.demo.work;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
@Order(40)
public class SpringSecurityConfig4 implements WebSecurityConfigurer, PriorityOrdered {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        System.out.println("http = " + http);
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .logout().permitAll()
                .and()
                .formLogin();
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
    }

    @Override
    public int getOrder() {
        return 40;
//        return LOWEST_PRECEDENCE;
    }
}
