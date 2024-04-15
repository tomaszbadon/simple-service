package net.beans.java.example.microservice.simple.web.socket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.beans.java.example.microservice.simple.model.GreetingInfo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    public void messageHandler(Object handler) {
        simpMessagingTemplate.convertAndSend("/topic/messages", new GreetingInfo("Hello World"));
    }

}
