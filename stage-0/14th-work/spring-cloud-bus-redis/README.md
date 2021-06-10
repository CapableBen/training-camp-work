#### 作业 利用 Redis 实现 Spring Cloud Bus 中的 BusBridge，避免强依赖于 Spring Cloud Stream
- 难点：处理消息订阅

- 目的：

  - 回顾 Spring 事件
  - 理解 Spring Cloud Bus 架构
  - 理解 Spring Cloud Stream

  ![image-20210610174736966](C:\Users\LP\AppData\Roaming\Typora\typora-user-images\image-20210610174736966.png)

  ###### 0.启动redis，修改每个项目配置文件 application.yaml 中 redis配置

  ###### 1.启动eureka，busprovider, busconsumer

  ###### 2.在teminal中执行：

  - curl --location --request POST "http://localhost:9990/actuator/busenv?name=name&value=xiaomge&destination=bus-consumer-1" --header "Content-Type: application/json"

    ```java
    // 系统默认入口： 设置某一个配置项 
    @Endpoint(id = "busenv")
    public class EnvironmentBusEndpoint extends AbstractBusEndpoint {
    	...
         // 该方法不知道改怎么访问？不知道在参数如何指定destination ？   
    	@WriteOperation
    	public void busEnvWithDestination(String name, String value,
    			@Selector(match = Match.ALL_REMAINING) String[] destinations) {
    		Map<String, String> params = Collections.singletonMap(name, value);
    		String destination = StringUtils.arrayToDelimitedString(destinations, ":");
    		publish(new EnvironmentChangeRemoteApplicationEvent(this, getInstanceId(), getDestination(destination),
    				params));
    	}
    
    	@WriteOperation
    	public void busEnv(String name, String value) {
    		Map<String, String> params = Collections.singletonMap(name, value);
    		publish(new EnvironmentChangeRemoteApplicationEvent(this, getInstanceId(), getDestination(null), params));
    	}
    }
    ```

  - curl --location --request POST "http://localhost:9990/actuator/busrefresh?destination=bus-consumer-1"

    ```java
    // 系统默认入口： 刷新所有绑定到刷新点的配置项
    @Endpoint(id = "busrefresh")
    public class RefreshBusEndpoint extends AbstractBusEndpoint {
        ...
        // 该方法不知道改怎么访问？不知道在参数如何指定destination ？   
       @WriteOperation
       public void busRefreshWithDestination(@Selector(match = Match.ALL_REMAINING) String[] destinations) {
          String destination = StringUtils.arrayToDelimitedString(destinations, ":");
          publish(new RefreshRemoteApplicationEvent(this, getInstanceId(), getDestination(destination)));
       }
    
       @WriteOperation
       public void busRefresh() {
          publish(new RefreshRemoteApplicationEvent(this, getInstanceId(), getDestination(null)));
       }
    
    }
    ```

  - curl --location --request GET "http://localhost:9090/?id=a&name=abcd&destination=bus-consumer-1"

    自定义 修改环境设置 入口 

前提：（项目中代码没有理解错）

个人疑问：（希望小马哥或者助教可以解答一下）

- 使用 cloud bus 如何指定destinations ？

- 自定义远程事件，应该如何接收事件对象 ？

- 已经存在 远程事件 和 自定义事件如何 整合消息订阅 ？

- busConsumer.accept() 之后 一般还要做什么操作 ？

  ```java
  // EnvironmentChangeRemoteApplicationEvent 因为事件被监听接收转换再发布以后，被存储在env中，还需要其他操作？
  public class EnvironmentChangeListener implements ApplicationListener<EnvironmentChangeRemoteApplicationEvent> {
  
     private static Log log = LogFactory.getLog(EnvironmentChangeListener.class);
  
     @Autowired
     private EnvironmentManager env;
  
     @Override
     public void onApplicationEvent(EnvironmentChangeRemoteApplicationEvent event) {
        Map<String, String> values = event.getValues();
        log.info("Received remote environment change request. Keys/values to update " + values);
        for (Map.Entry<String, String> entry : values.entrySet()) {
           this.env.setProperty(entry.getKey(), entry.getValue());
        }
     }
  }
  ```



