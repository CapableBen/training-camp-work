1. 进入user-platform目录

2. mvn clean package -U

3. java -jar user-web/target/user-web-v1-SNAPSHOT-war-exec.jar

4. 需求二（选做）

   测试访问：http://localhost:8080/config/test

   动态访问：http://localhost:8080/config/get?application.name&application.version=integer&java.vm.specification.version=double&tomcat.util.buf.StringCache.byte.enabled=boolean

5. 需求一（必须）

   读：http://localhost:8080/jolokia/read/org.geektimes.projects.user.management:type=User

   写：http://localhost:8080/jolokia/write/org.geektimes.projects.user.management:type=User/Name/damage

   执行：http://localhost:8080/jolokia/exec/org.geektimes.projects.user.management:type=User/toString

   列表：http://localhost:8080/jolokia/list