package net.beans.java.example.microservice.simple.ws;

import com.nimbusds.openid.connect.sdk.assurance.evidences.VerificationMethod;
import lombok.extern.slf4j.Slf4j;
import net.beans.java.example.microservice.simple.api.SimpleMicroserviceResourceBaseTest;
import net.beans.java.example.microservice.simple.model.GreetingInfo;
import net.beans.java.example.microservice.simple.web.socket.controller.WebSocketController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Slf4j
public class SimpleWebSocketTest extends SimpleMicroserviceResourceBaseTest {

    @SpyBean
    private SimpleStompSessionHandler stompSessionHandler;

    @SpyBean
    private WebSocketController webSocketController;
    private static final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

    @Test
    void webSocketTest(@LocalServerPort int port) throws Exception {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(converter);

        CompletableFuture<StompSession> future = stompClient.connectAsync(String.format("ws://localhost:%s/api/websocket", port), stompSessionHandler);
        StompSession session = future.get(10, TimeUnit.SECONDS);
        session.subscribe("/topic/messages", stompSessionHandler);
        session.send("/app/hello", "Hello World");

        Thread.sleep(1000);
        Mockito.verify(webSocketController, Mockito.times(1)).messageHandler(any());
        Mockito.verify(stompSessionHandler, Mockito.times(1)).handleFrame(any(), any(GreetingInfo.class));
        Mockito.verify(stompSessionHandler, Mockito.never()).handleException(any(), any(), any(), any(), any(Throwable.class));
        Mockito.verify(stompSessionHandler, Mockito.never()).handleTransportError(any(), any(Throwable.class));
    }

}
