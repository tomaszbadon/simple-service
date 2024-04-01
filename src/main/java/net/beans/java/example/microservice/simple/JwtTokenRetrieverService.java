package net.beans.java.example.microservice.simple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.DefaultMapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class JwtTokenRetrieverService {

    private final static String REFRESH_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:refresh_token";
    private static final DefaultMapOAuth2AccessTokenResponseConverter MAP_TO_OAUTH_TOKEN_CONVERTER = new DefaultMapOAuth2AccessTokenResponseConverter();

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String keycloackClientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String keycloackClientSecret;

    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
    private String keycloakTokenUri;

    public OAuth2AccessTokenResponse authorizeUser(String username, String password) {
        // Build a map of our request parameters
        Map<String, String> requestParams = buildReqParams(AuthorizationGrantType.PASSWORD.getValue(), REFRESH_TOKEN_TYPE);
        requestParams.put("username", username);
        requestParams.put("password", password);
        return sendRequest(requestParams);
    }

    private OAuth2AccessTokenResponse sendRequest(Map<String, String> requestParams) {
        // URL form encode our parameters in preparation to send them.
        String formEncodedBody = requestParams.entrySet()
                .stream()
                .map(entry -> String.join("=",
                        URLEncoder.encode(entry.getKey(), UTF_8),
                        URLEncoder.encode(entry.getValue(), UTF_8))
                ).collect(Collectors.joining("&"));

        // Build the request.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(keycloakTokenUri))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(formEncodedBody))
                .timeout(Duration.ofSeconds(4))
                .build();

        HttpClient.Builder builder = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .followRedirects(HttpClient.Redirect.NORMAL);

        // Make the token request.
        HttpResponse<String> result = builder.build()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();

        // Check the status code to see if it is a successful response...
        if (result.statusCode() != HttpStatus.OK.value()) {
            // It isn't so throw an access denied exception.
            throw new AccessDeniedException("Invalid access token provided: ("+result.statusCode()+")" + result.body());
        }

        try {
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {
            };
            // Convert the json string to a map.
            Map<String, Object> mapOauth = objectMapper.readValue(result.body(), typeRef);

            // Convert the map to a OAuth2AccessTokenResponse
            return MAP_TO_OAUTH_TOKEN_CONVERTER.convert(mapOauth);
        } catch (JsonProcessingException ex) {
            // Catching and rethrowing here to reflect the correct end state of the action; which is we don't have a
            // valid token response.
            throw new AccessDeniedException("Invalid token response from identity management provider", ex);
        }
    }

    private Map<String, String> buildReqParams(String grantType, String requestedTokenType) {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("client_id", keycloackClientId);
        requestParams.put("grant_type", grantType);
        requestParams.put("requested_token_type", requestedTokenType);
        requestParams.put("client_secret", keycloackClientSecret);
        return requestParams;
    }

}
