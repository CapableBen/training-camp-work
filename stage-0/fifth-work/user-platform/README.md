1. 启动 activemq

2. 进入user-platform目录

3. mvn clean package -U

4. java -jar user-web/target/user-web-v1-SNAPSHOT-war-exec.jar

5. ###### 修复本程序 org.geektimes.reactive.streams 包下

    运行my-reactive-messaging模块：
    src/test/java/org/geektimes/reactive/streams/DefaultPublisher.java

6. ###### 继续完善 my-rest-client POST 方法

   运行my-rest-client模块：
   src/test/java/org/geektimes/rest/demo/RestClientDemo.java