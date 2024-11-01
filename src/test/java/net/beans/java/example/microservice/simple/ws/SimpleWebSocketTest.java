package net.beans.java.example.microservice.simple.ws;

import lombok.extern.slf4j.Slf4j;
import net.beans.java.example.microservice.simple.controller.rest.SimpleMicroserviceResourceBaseTest;
import net.beans.java.example.microservice.simple.model.GreetingInfo;
import net.beans.java.example.microservice.simple.web.socket.controller.WebSocketController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Slf4j
public class SimpleWebSocketTest extends SimpleMicroserviceResourceBaseTest {

    @SpyBean
    private SimpleStompSessionHandler stompSessionHandler;

    @SpyBean
    private WebSocketController webSocketController;

    @Autowired
    @LocalServerPort
    int port;

    private static final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

    private static final String URI = "ws://localhost:%s/api/websocket";


    private WebSocketClient client;

    private WebSocketStompClient stompClient;

    @BeforeEach
    void beforeEach() {
        client = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(converter);
    }


    @ParameterizedTest
    @MethodSource("headers")
    void webSocketSecurityTest(WebSocketHttpHeaders httpHeaders, StompHeaders stompHeaders) {
        StompSession session = null;
        try {
            var future = stompClient.connectAsync(String.format(URI, port), httpHeaders, stompHeaders, stompSessionHandler);
            session = future.get(10, TimeUnit.SECONDS);
            session.subscribe("/topic/messages", stompSessionHandler);
            session.send("/app/hello", "Hello World");

            Thread.sleep(1000);

            Mockito.verify(webSocketController, Mockito.times(1)).messageHandler(any());
            Mockito.verify(stompSessionHandler, Mockito.times(1)).handleFrame(any(), any(GreetingInfo.class));
            Mockito.verify(stompSessionHandler, Mockito.never()).handleException(any(), any(), any(), any(), any(Throwable.class));
            Mockito.verify(stompSessionHandler, Mockito.never()).handleTransportError(any(), any(Throwable.class));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage(), e);
        } finally {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    @Test
    void authorisationTest() {
        assertThatThrownBy(() -> {
            var future = stompClient.connectAsync(String.format(URI, port), stompSessionHandler);
            StompSession session = future.get(10, TimeUnit.SECONDS);
        }).isInstanceOf(ExecutionException.class).rootCause().isInstanceOf(ConnectionLostException.class).hasMessageContaining("Connection closed");
    }

    private static Stream<Arguments> headers() {
        WebSocketHttpHeaders httpHeaders = new WebSocketHttpHeaders();
        httpHeaders.add(WebSocketHttpHeaders.AUTHORIZATION, MessageFormat.format("Bearer {0}", ACCESS_TOKEN));

        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add(WebSocketHttpHeaders.AUTHORIZATION, MessageFormat.format("Bearer {0}", ACCESS_TOKEN));

        return Stream.of(Arguments.of(httpHeaders, new StompHeaders()), Arguments.of(new WebSocketHttpHeaders(), stompHeaders));
    }
}
