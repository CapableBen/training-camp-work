package com.geekbang.springmybatis;

import com.geekbang.springmybatis.work.annotation.EnableMyBatis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableMyBatis(dataSource = "dataSource",
        configLocation = "classpath:mybatis/mybatis-config.xml",
        mapperLocations = {"classpath:com/**/*.xml"},
        environment = "development",
		plugins = {"myInterceptor"})
//@ImportResource(locations = "classpath*:sample/spring-context.xml") // SqlSessionFactoryBean
public class SpringMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMybatisApplication.class, args);
    }

}
