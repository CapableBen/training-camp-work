1. 进入user-platform目录

2. mvn clean package -U

3. java -jar user-web/target/user-web-v1-SNAPSHOT-war-exec.jar

4. ###### 完善 my dependency-injection 模块

   - 脱离 web.xml 配置实现 ComponentContext 自动初始化

   - 使用独立模块并且能够在 user-web 中运行成功

    访问：http://localhost:8080/test/component

5. ###### 完善 my-configuration 模块

   - Config 对象如何能被 my-web-mvc 使用
   - 可能在 ServletContext 获取
   - 如何通过 ThreadLocal 获取

    访问：http://localhost:8080/test/config?application.name