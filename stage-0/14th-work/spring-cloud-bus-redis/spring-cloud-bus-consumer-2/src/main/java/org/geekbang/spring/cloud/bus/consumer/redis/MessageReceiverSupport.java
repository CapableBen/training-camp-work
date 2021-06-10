package org.geekbang.spring.cloud.bus.consumer.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.bus.BusConsumer;
import org.springframework.cloud.bus.event.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import javax.lang.model.SourceVersion;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-06-09
 */
@Component
public class MessageReceiverSupport implements MessageListener {

    @Autowired
    private BusConsumer busConsumer;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topic = new String(message.getChannel(), StandardCharsets.UTF_8);
        System.out.println("bus-consumer-2 = " + topic);
        String content = new String(message.getBody(), StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        RemoteApplicationEvent remoteApplicationEvent = null;
        try {
//            hashMap = {
//            destinationService=destination, values=[java.util.HashMap, {name=xxx, id=1}],
//            originService=provider-node1, id=3ef89a6d-9739-491e-8ef9-2e28734ddf85,
//            type=EnvironmentChangeRemoteApplicationEvent, timestamp=1623291319524
//            }
            Map<String, Object> hashMap = objectMapper.readValue(content, HashMap.class);

            String originService = (String) hashMap.get("originService");
            String destinationService = (String) hashMap.get("destinationService");

            if (!"*".equalsIgnoreCase(destinationService) && !"**".equalsIgnoreCase(destinationService)) {
                if (StringUtils.isEmpty(applicationName)
                        || StringUtils.isEmpty(destinationService)
                        || !applicationName.equalsIgnoreCase(destinationService)) {
                    return;
                }
            }

            String eventType = (String) hashMap.get("type");

            boolean notEmpty = StringUtils.isNotEmpty(eventType);
            if (!notEmpty) {
                return;
            }

            switch (eventType) {
                case "EnvironmentChangeRemoteApplicationEvent":
                    ArrayList<Map<String, String>> values = (ArrayList<Map<String, String>>) hashMap.get("values");
                    remoteApplicationEvent = new EnvironmentChangeRemoteApplicationEvent(this, originService, () -> destinationService, values.get(1));
                    break;
                case "AckRemoteApplicationEvent":
                    // TODO
                    break;
                case "RefreshRemoteApplicationEvent":
                    remoteApplicationEvent = new RefreshRemoteApplicationEvent(this, originService, () -> destinationService);
                    break;
                case "UnknownRemoteApplicationEvent":
                    // TODO
                    break;
                default:
                    // TODO custom event
                    remoteApplicationEvent = objectMapper.readValue(content, RemoteApplicationEvent.class);
                    break;
            }
        } catch (JsonProcessingException e) {
            System.out.println("e = " + e);
            return;
        }

        busConsumer.accept(remoteApplicationEvent);
        System.out.println("bus-consumer-2 处理远程事件完成!");
    }
}
