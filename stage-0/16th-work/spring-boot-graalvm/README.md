#### 作业 将 Spring Boot 应用打包 Java Native 应用，再将该应用通过
Dockerfile 构建 Docker 镜像，部署到 Docker 容器中，并且
成功运行，Spring Boot 应用的实现复杂度不做要求

##### 测试：

- 0.本地安装docker环境

- 1.在teminal中执行：docker pull capableben/spring-boot-graalvm:0.0.1-SNAPSHOT

- 2.在teminal中执行：docker run -p 8080:8080 capableben/spring-boot-graalvm:0.0.1-SNAPSHOT

- 3.新开teminal执行：

  - curl --location --request GET "http://localhost:8080/"
  



##### 结合上次作业 总结：



##### 1.通过 spring-boot:build-image 生成docker image

需要在mvn package生成spring-boot-graalvm-0.0.1-SNAPSHOT.jar.original文件（spring-boot-maven-plugin 打包插件 classifier 不能指定）

```shell
mvn spring-boot:build-image

docker-compose up
```



##### 2.通过自定义 native (native-image-maven-plugin)插件，生成可执行文件（windows平台exe）：

```xml
 <profiles>
        <profile>
            <id>native</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.graalvm.nativeimage</groupId>
                        <artifactId>native-image-maven-plugin</artifactId>
                        <version>${native-image-maven-plugin.version}</version>
                        <configuration>
                            <!-- -J-Xmx4G -H:+ReportExceptionStackTraces-->
                            <buildArgs>${native.build.args}</buildArgs>
                            <imageName>${project.artifactId}</imageName>
                        </configuration>
                        <executions>
                            <execution>
                                <goals>
                                    <goal>native-image</goal>
                                </goals>
                                <phase>package</phase>
                            </execution>
                        </executions>
                    </plugin>
                    <plugin>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-maven-plugin</artifactId>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
```

  ```shell
  mvn -Pnative package
  ```



##### 3.通过Dockerfile 构建 docker image

```shell
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} spring-boot-graalvm.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /spring-boot-graalvm.jar"]
```

```shell
mvn clean package -U  # 打包

java -jar target/spring-boot-graalvm-0.0.1-SNAPSHOT.jar  # 测试

docker build -t capableben/spring-boot-graalvm:0.0.1-SNAPSHOT . # 构建

docker run -p 8080:8080 capableben/spring-boot-graalvm:0.0.1-SNAPSHOT # 执行

docker tag spring-boot-graalvm  capableben/spring-boot-graalvm # 改名

curl --location --request GET "http://localhost:8080/" # 测试

docker push capableben/spring-boot-graalvm:0.0.1-SNAPSHOT # 推送

docker pull capableben/spring-boot-graalvm:0.0.1-SNAPSHOT # 拉取
```

