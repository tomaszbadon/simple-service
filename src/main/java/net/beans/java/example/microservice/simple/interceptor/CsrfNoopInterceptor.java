package net.beans.java.example.microservice.simple.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component("csrfChannelInterceptor")
public class CsrfNoopInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        return message;
    }
}


// https://stackoverflow.com/questions/75068726/setting-up-csrf-for-spring-websocket