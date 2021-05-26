package com.geekbang.springmybatis.autoconfigure.autoconfiguration;

import com.geekbang.springmybatis.autoconfigure.properties.MybatisProperties;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-05-25
 */
@Configuration
@EnableConfigurationProperties(value = MybatisProperties.class)
// 说明： @EnableMyBatis 注解方式 需要 Properties className 作为参数，进行SqlSessionFactoryBean的实例化
//        并没有在比当前 AutoConfiguration 进行配置更优，操作配置更加麻烦。
//@EnableMyBatis(dataSource = "dataSource",
//        configLocation = "classpath:/**/*.xml", //
//        mapperLocations = "classpath:/**/mappers/*.xml", //
//        environment = "development", //
//        plugins = "myInterceptor") //
public class MybatisAutoConfiguration implements InitializingBean, EnvironmentAware {

    private Environment environment;

    @Autowired
    private MybatisProperties mybatisProperties;

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    @Autowired(required = false)
    private Interceptor[] interceptors;

    @Bean(name = "dataSource")
    @ConditionalOnMissingBean
    public DataSource setDataSource(MybatisProperties mybatisProperties) {
        DataSource dataSource = new UnpooledDataSource(mybatisProperties.getDriver(),
                mybatisProperties.getUrl(), mybatisProperties.getUsername(), mybatisProperties.getPassword());
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(final DataSource dataSource, final MybatisProperties properties) throws Exception {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        sqlSessionFactoryBean.setMapperLocations(properties.getMapperLocations());
        sqlSessionFactoryBean.setEnvironment(properties.getEnvironment());
        // interceptors 需要配置注入ioc
        sqlSessionFactoryBean.setPlugins(interceptors);

        // setConfigLocation 只能通过xml配置 或者 配置属性
        // 二选一： config-location: classpath:mybatis/mybatis-config.xml / configuration
//        sqlSessionFactoryBean.setConfigLocation(properties.getConfigLocation());
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//        configuration.setLogImpl(StdOutImpl.class);//标准输出日志
        configuration.setLogImpl(Log4jImpl.class);
//        configuration.setLogImpl(Slf4jImpl.class);
//        configuration.setLogImpl(NoLoggingImpl.class);// 不输出日志（）
        configuration.setMapUnderscoreToCamelCase(true);// 开启驼峰命名
        configuration.setCallSettersOnNulls(true);// 开启在属性为null也调用setter方法
        // TODO hardcode -> 通过 配置文件 动态设置其他属性
        sqlSessionFactoryBean.setConfiguration(configuration);

//        sqlSessionFactoryBean.setTypeAliasesPackage(properties.getAliasesPackage());
//        sqlSessionFactoryBean.setTypeHandlersPackage(properties.getTypeHandlersPackage());

        return sqlSessionFactoryBean.getObject();
    }

    private Object resolvePlaceholder(Object value) {
        if (value instanceof String) {
            return environment.resolvePlaceholders((String) value);
        }
        return value;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private Resource[] getResources(String location) {
        try {
            return resourceResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(final SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
