0. 在github上创建OAuth Apps, (配置可参考src/main/webapp/OAuthAppSetting.jpg)
    - ​ Homepage URL: http://localhost:8080/oauth/login
    - ​ Authorization callback URL: http://localhost:8080/oauth/callback

1. 配置成功后
    - ​ 修改 GitHubCallBackController 中静态变量: CLIENT_ID
    - ​ 修改 GitHubCallBackController 中静态变量: CLIENT_SECRET
    - ​ 修改 GitHubCallBackController 中静态变量: CALLBACK
    - ​ 修改 src/main/webapp/login.jsp 中a标签连接参数: client_id 和 redirect_uri 

2. 进入user-platform目录

3. mvn clean package -U

4. java -jar user-web/target/user-web-v1-SNAPSHOT-war-exec.jar

5. 访问：http://localhost:8080/oauth/login
    - ​ 如果遇到org.apache.http.conn.HttpHostConnectException: Connect to github.com:443 [github.com/192.30.255.112] failed: Connection timed out: connect 请开代理 或者 重试.

