package com.geekbang.springmybatis.work.service;

import com.geekbang.springmybatis.work.domain.User;
import com.geekbang.springmybatis.work.mappers.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.util.List;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-05-11
 */
@Service
@Transactional
public class UserServiceImpl {

    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @Autowired(required = false)
    private void setSqlSessionFactoryBean(SqlSessionFactoryBean sqlSessionFactoryBean) {
        this.sqlSessionFactoryBean = sqlSessionFactoryBean;
        // TODO SqlSession 抽象
    }

    public User selectById(String id) {
        try {
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
            assert sqlSessionFactory != null;
            try (SqlSession session = sqlSessionFactory.openSession()) {

                UserMapper userMapper = session.getMapper(UserMapper.class);
                // 你的应用逻辑代码
                User user = userMapper.selectUser(id);
                userMapper.selectByMapping(id);
                return user;
            }
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        return null;
    }

    public List<User> selectUsers() {
        try {
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
            assert sqlSessionFactory != null;
            try (SqlSession session = sqlSessionFactory.openSession()) {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                // 你的应用逻辑代码
                return userMapper.selectUsers();
            }
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        return null;
    }

    public void addUsers() {
        try {
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
            assert sqlSessionFactory != null;
            try (SqlSession session = sqlSessionFactory.openSession()) {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                // 你的应用逻辑代码
                userMapper.dropUsers();
                userMapper.createUsers();
                userMapper.insertUsers();
            }
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
//        throw new RuntimeException("...........");
    }

}
