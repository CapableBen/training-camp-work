#### 作业 Spring Mybatis 注解驱动

- org.geektimes.projects.user.mybatis.annotation.EnableMyBatis 实现，
  * 尽可能多地注入org.mybatis.spring.SqlSessionFactoryBean 中依赖的组件

###### 0.启动 mysql 

###### 1.修改src/main/resources/application.properties数据库配置文件

###### 2.启动spring-mybatis项目（spring boot项目）

###### 3.访问

- http://localhost:8080/add

   初始化

- http://localhost:8080/list

   获取全部
   
- http://localhost:8080/get?id=1

   根据id查询

###### 个人疑惑：

基本实现注解驱动，但是在 ```自行添加其他属性``` 

- 不太懂怎么添加数组对象类型的属性？

- beanDefinitionBuilder.addPropertyReference的参数为String，是否存在其他api？
- 事务处理应该怎么处理？

```
private TransactionFactory transactionFactory;

private Interceptor[] plugins;

private TypeHandler<?>[] typeHandlers;

```

如果可以，希望马哥能写一份完整一点的实现，因为第一次接触注解驱动，希望能得到一个全面一些的例子。