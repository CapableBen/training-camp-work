#### 作业 通过 GraalVM 将一个简单 Spring Boot 工程构建为 Native Image


##### 要求：

- 代码要自己手写 @Controller @RequestMapping("/helloworld")

- 相关插件可以参考 Spring Native Samples

- （可选）理解 Hint 注解的使用



##### 测试：

- 0.在teminal中执行：mvn -Pnative package

- 1.进入target目录 执行：spring-boot-graalvm

- 2.新开teminal执行：

  - curl --location --request GET "[http://localhost:8080/"](http://localhost:8080/)



##### GraalVM window安装 步骤：

- GraalVM： https://github.com/graalvm/graalvm-ce-builds/releases 下载解压即可

  ![image-20210616151314544](C:\Users\LP\AppData\Roaming\Typora\typora-user-images\image-20210616151314544.png)

- visualstudio2019： https://visualstudio.microsoft.com/zh-hans/vs/ 下载并安装

  ![image-20210616151600157](C:\Users\LP\AppData\Roaming\Typora\typora-user-images\image-20210616151600157.png)

  ![image-20210616151624108](C:\Users\LP\AppData\Roaming\Typora\typora-user-images\image-20210616151624108.png)

  ![image-20210616151706868](C:\Users\LP\AppData\Roaming\Typora\typora-user-images\image-20210616151706868.png)

  ![image-20210616151734401](C:\Users\LP\AppData\Roaming\Typora\typora-user-images\image-20210616151734401.png)

![image-20210616151811560](C:\Users\LP\AppData\Roaming\Typora\typora-user-images\image-20210616151811560.png)

- 以 ``x64 Native Tools Command Prompt for VS 2019``  或者 配置环境（安装vs2019 会生成Windows Kits库）后任意doc启动 

    // 在系统环境变量加入变量

    - INCLUDE=D:\Windows Kits\10\Include\10.0.19041.0\ucrt;D:\Windows Kits\10\Include\10.0.19041.0\um;D:\Windows Kits\10\Include\10.0.19041.0\shared;D:\install\VisualStudio\2019\Community\VC\Tools\MSVC\14.16.27023\include.;

    - LIB=D:\Windows Kits\10\Lib\10.0.19041.0\um\x64;D:\Windows Kits\10\Lib\10.0.19041.0\ucrt\x64;D:\install\VisualStudio\2019\Community\VC\Tools\MSVC\14.16.27023\lib\x64;

    - VisualStudio_HOME=D:\install\VisualStudio\2019\Enterprise\VC\Tools\MSVC\14.16.27023\bin\HostX64\x64
      GRAALVM_HOME=D:\install\graalvm-ce-java8-21.1.0

    //修改path路径 （假如之前存在 hotspot配置 需要把GRAALVM放在最前或者删除之前配置）

    - %VisualStudio_HOME%;%GRAALVM_HOME%\bin;%GRAALVM_HOME%\jre\bin;

- 配置成功后执行 gu install  native-image

- 编写HelloWorld.java，执行 native-image HelloWorld

- 生成HelloWorld.exe 运行测试

##### spring-native项目maven配置

- pom

  参考代码pom，关键在于配置native-image-maven-plugin和版本

- 在生成镜像时，可能由于缓存，还是使用hotspot vm 导致失败（环境变量调整）。

  本地安装docker：

  ```
  # 通过build-image生成dockerimage，需要在mvn package生成spring-boot-graalvm-0.0.1-SNAPSHOT.jar.original文件
  # spring-boot-maven-plugin 打包插件 classifier 不能指定
  mvn spring-boot:build-image
  docker-compose up
  ```

  直接执行打包和生成镜像：

  ```
  mvn -Pnative package
  ```

- Hint 使用

  // todo 注解，api具体使用细节

  ![image-20210616153635685](C:\Users\LP\AppData\Roaming\Typora\typora-user-images\image-20210616153635685.png)