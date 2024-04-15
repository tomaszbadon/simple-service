package net.beans.java.example.microservice.simple.ws;

import lombok.extern.slf4j.Slf4j;
import net.beans.java.example.microservice.simple.model.GreetingInfo;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Slf4j
public class SimpleStompSessionHandler implements StompSessionHandler {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {

    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.info("After Connection");
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return GreetingInfo.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {

    }
}
