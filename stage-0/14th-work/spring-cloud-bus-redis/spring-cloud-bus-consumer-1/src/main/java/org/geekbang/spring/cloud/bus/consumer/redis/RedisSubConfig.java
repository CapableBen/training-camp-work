package org.geekbang.spring.cloud.bus.consumer.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-09
 */
@Configuration
@AutoConfigureAfter({MessageReceiverSupport.class})
public class RedisSubConfig {

    @Value("${topic.name}")
    private String topicName;

    /**
     * Redis消息监听器容器
     * 这个容器加载了RedisConnectionFactory和消息监听器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     *
     * @param connectionFactory 链接工厂
     * @param adapter           适配器
     * @return redis消息监听容器
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 可以添加多个 messageListener
        // TODO messageListeners, topics
        container.addMessageListener(adapter, new PatternTopic(topicName));
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * 将MessageReceiver注册为一个消息监听器，可以自定义消息接收的方法（handleMessage）
     * 如果不指定消息接收的方法，消息监听器会默认的寻找MessageReceiver中的onMessage这个方法作为消息接收的方法
     *
     * @param messageReceiver 消息接受
     * @return 适配器
     */
    @Bean
    public MessageListenerAdapter adapter(MessageReceiverSupport messageReceiver) {
        return new MessageListenerAdapter(messageReceiver, "onMessage");
    }
}

