package com.geekbang.springmybatis.sample.mappers;

import com.geekbang.springmybatis.sample.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-05-12
 */
public interface UserMapper {

    public static final String DROP_USERS_TABLE_DDL_MY_SQL = "DROP TABLE IF EXISTS users";

    public static final String CREATE_USERS_TABLE_DDL_MY_SQL = "CREATE TABLE users(" +
            "id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
            "name VARCHAR(16) NOT NULL, " +
            "password VARCHAR(64) NOT NULL, " +
            "email VARCHAR(64) NOT NULL, " +
            "phoneNumber VARCHAR(64) NOT NULL" +
            ")";

    public static final String INSERT_USER_DML_SQL = "INSERT INTO users(name,password,email,phoneNumber) VALUES " +
            "('A','******','a@gmail.com','1') , " +
            "('B','******','b@gmail.com','2') , " +
            "('C','******','c@gmail.com','3') , " +
            "('D','******','d@gmail.com','4') , " +
            "('E','******','e@gmail.com','5')";

    @Update(DROP_USERS_TABLE_DDL_MY_SQL)
    void dropUsers();

    @Update(CREATE_USERS_TABLE_DDL_MY_SQL)
    void createUsers();

    @Insert(INSERT_USER_DML_SQL)
    void insertUsers();

    @Select("SELECT * FROM users")
    List<User> selectUsers();

    User selectByMapping(@Param("id") String id);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User selectUser(@Param("id") String id);

}

