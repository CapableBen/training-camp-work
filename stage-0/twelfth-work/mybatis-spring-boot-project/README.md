#### 作业 自定义mybatis-spring-boot-starter

- 将上次 MyBatis @Enable 模块驱动，封装成 Spring Boot Starter ⽅式
  参考：MyBatis Spring Project ⾥⾯会有 Spring Boot 实现  

###### 0.启动 mysql 

###### 1.修改 src/main/resources/application.properties 数据库配置文件

###### 2.启动 mybatis-spring-boot-starter-sample 项目

###### 3.访问

- http://localhost:8080/add

   初始化

- http://localhost:8080/list

   获取全部
   
- http://localhost:8080/get?id=1

   根据id查询
   
说明： 假如需要引入到其他项目，需要先install mybatis-spring-boot-starter 模块，然后引入并配置