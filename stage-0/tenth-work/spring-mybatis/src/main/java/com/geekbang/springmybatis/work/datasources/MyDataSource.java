package com.geekbang.springmybatis.work.datasources;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-05-12
 */
@Configuration
public class MyDataSource {

    @Value("${jdbc.driver}")
    private String driver;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Bean(name = "dataSource")
    public DataSource setDataSource() {
        DataSource dataSource = new UnpooledDataSource(driver, url, username, password);
        return dataSource;
    }
}
