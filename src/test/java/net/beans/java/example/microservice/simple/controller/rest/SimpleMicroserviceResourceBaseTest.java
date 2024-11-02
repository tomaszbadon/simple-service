package net.beans.java.example.microservice.simple.controller.rest;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static java.text.MessageFormat.format;
import static org.springframework.test.util.AssertionErrors.fail;

@Slf4j
public class SimpleMicroserviceResourceBaseTest {

    public static final String CLIENT_SECRET = "Y1JUH3DVEeZNXKz9UsRH3Y3SyOLAtkNb";
    public static final String CLENT_ID = "simple-microservice";
    public static final String REALM_NAME = "simple-application-realm";
    public static String ACCESS_TOKEN;
    private static final Network network = Network.newNetwork();

    @Container
    private static final KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:24.0.2")
            .withEnv("DB_VENDOR", "h2")
            .withAdminUsername("admin")
            .withAdminPassword("admin")
            .withContextPath("/auth")
            .withNetwork(network)
            .withRealmImportFile("/simple-application-realm.json");

    @Container
    private static final MySQLContainer mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:9.1.0"))
            .withDatabaseName("simple-service")
            .withUsername("test-user")
            .withPassword("test")
            .withNetwork(network);

    @DynamicPropertySource
    static void updateProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/simple-application-realm");
        registry.add("spring.security.oauth2.client.provider.keycloak.token-uri", () -> keycloak.getAuthServerUrl() + "/realms/simple-application-realm/protocol/openid-connect/token");
        registry.add("spring.security.oauth2.client.registration.keycloak.client-id", () -> CLENT_ID);
        registry.add("spring.security.oauth2.client.registration.keycloak.client-secret", () -> CLIENT_SECRET);
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @BeforeAll
    static void acquireToken() {

//        mysqlContainer.start();

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
            ACCESS_TOKEN = accessTokenResponse.getToken();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @AfterAll
    static void token() {
        ACCESS_TOKEN = null;
    }

}
