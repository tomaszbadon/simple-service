package net.beans.java.example.microservice.simple.api;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.util.AssertionErrors.fail;

@Slf4j
public class SimpleMicroserviceResourceBaseTest {

    public static final String CLIENT_SECRET = "Y1JUH3DVEeZNXKz9UsRH3Y3SyOLAtkNb";
    public static final String CLENT_ID = "simple-microservice";
    public static final String REALM_NAME = "simple-application-realm";
    public static String ACQUIRED_TOKEN;

    @Container
    private static final KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:24.0.2")
            .withEnv("DB_VENDOR", "h2")
            .withAdminUsername("admin")
            .withAdminPassword("admin")
            .withContextPath("/auth")
            .withRealmImportFile("/simple-application-realm.json");

    @DynamicPropertySource
    static void updateProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/simple-application-realm");
        registry.add("spring.security.oauth2.client.provider.keycloak.token-uri", () -> keycloak.getAuthServerUrl() + "/realms/simple-application-realm/protocol/openid-connect/token");
        registry.add("spring.security.oauth2.client.registration.keycloak.client-id", () -> CLENT_ID);
        registry.add("spring.security.oauth2.client.registration.keycloak.client-secret", () -> CLIENT_SECRET);
    }

    @BeforeAll
    static void acquireToken() {
        try (var client = KeycloakBuilder.builder()
                .serverUrl(keycloak.getAuthServerUrl())
                .realm(REALM_NAME)
                .clientId(CLENT_ID)
                .clientSecret(CLIENT_SECRET)
                .grantType(OAuth2Constants.PASSWORD)
                .username("test")
                .password("test")
                .build()) {
            AccessTokenResponse accessTokenResponse = client.tokenManager().getAccessToken();
            ACQUIRED_TOKEN = accessTokenResponse.getToken();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @AfterAll
    static void token() {
        ACQUIRED_TOKEN = null;
    }

}
