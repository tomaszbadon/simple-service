package net.beans.java.example.microservice.simple.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements ChannelInterceptor {

    private final JwtIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver;

    private final static String BEARER = "Bearer ";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (Objects.isNull(accessor) || !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }
        try {
            //Extract JWT token from header, validate it and extract user authorities
            var token = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
            if (Objects.isNull(token) || token.length() < BEARER.length()) {
                return message;
            }

            // HTTP Servlet request is not used so it is safe for it to be null.
            var authManager = jwtIssuerAuthenticationManagerResolver.resolve(null);

            BearerTokenAuthenticationToken bearerToken = new BearerTokenAuthenticationToken(token.substring(BEARER.length()));
            Authentication user = authManager.authenticate(bearerToken);
            accessor.setUser(user);
        } catch (Exception e) {
            log.error("Error verifying JWT token on STOMP CONNECT message", e);
        }

        return message;
    }
}
