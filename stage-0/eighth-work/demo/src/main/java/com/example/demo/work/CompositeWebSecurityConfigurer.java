package com.example.demo.work;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class CompositeWebSecurityConfigurer extends WebSecurityConfigurerAdapter  {

    private List<WebSecurityConfigurer> webSecurityConfigurers;

    @Autowired(required = false)
    private void setWebSecurityConfigurers(List<WebSecurityConfigurer> webSecurityConfigurers) {
        System.out.println("webSecurityConfigurers = " + webSecurityConfigurers);
        this.webSecurityConfigurers = webSecurityConfigurers;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        webSecurityConfigurers.forEach(c -> {
            try {
                c.configure(http);
            } catch (Exception e) {
                System.out.println("e = " + e);
            }
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        webSecurityConfigurers.forEach(c -> {
            try {
                c.configure(web);
            } catch (Exception e) {
                System.out.println("e = " + e);
            }
        });
    }
}
