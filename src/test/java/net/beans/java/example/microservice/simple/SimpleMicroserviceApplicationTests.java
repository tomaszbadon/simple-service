package net.beans.java.example.microservice.simple;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SimpleMicroserviceApplicationTests {

    @Autowired
    private JwtTokenRetrieverService tokenRetrieverService;

    @Autowired
    private TestRestTemplate template;

    @Container
    private static final KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:24.0")
            .withEnv("DB_VENDOR", "h2")
            .withAdminUsername("admin")
            .withAdminPassword("admin")
            .withContextPath("/auth")
            .withRealmImportFile("/simple-application-realm.json");

    @DynamicPropertySource
    static void updateProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/simple-application-realm");
        registry.add("spring.security.oauth2.client.provider.keycloak.token-uri", () -> keycloak.getAuthServerUrl() + "/realms/simple-application-realm/protocol/openid-connect/token");
        registry.add("spring.security.oauth2.client.registration.keycloak.client-id", () -> "simple-microservice");
        registry.add("spring.security.oauth2.client.registration.keycloak.client-secret", () -> "Y1JUH3DVEeZNXKz9UsRH3Y3SyOLAtkNb");
    }

    @Test
    void authorisationTest() {
        String accessToken = tokenRetrieverService.authorizeUser("test", "test").getAccessToken().getTokenValue();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		var response = template.exchange("/hello", HttpMethod.GET, new HttpEntity<>(httpHeaders), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
    }

}
