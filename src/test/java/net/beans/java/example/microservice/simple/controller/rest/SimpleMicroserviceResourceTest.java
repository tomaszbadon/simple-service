package net.beans.java.example.microservice.simple.controller.rest;

import lombok.extern.slf4j.Slf4j;
import net.beans.java.example.microservice.simple.model.GreetingInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SimpleMicroserviceResourceTest extends SimpleMicroserviceResourceBaseTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    @DisplayName("Authorisation Test Without Token")
    void authorisationWithoutTokenTest() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var response = template.exchange("/api/greetings/info", HttpMethod.GET, new HttpEntity<>(httpHeaders), GreetingInfo.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(401));
    }

    @Test
    @DisplayName("Authorisation Test With Token - Role not needed")
    void authorisationWithTokenTest() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN);
        var response = template.exchange("/api/greetings/info", HttpMethod.GET, new HttpEntity<>(httpHeaders), GreetingInfo.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(new GreetingInfo("Hello World!"));
    }

    @Test
    @DisplayName("Authorisation Test With Token - Role ApplicationUser needed")
    void authorisationWithApplicationRoleTest() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN);
        var response = template.exchange("/api/greetings/message", HttpMethod.GET, new HttpEntity<>(httpHeaders), GreetingInfo.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(new GreetingInfo("Hello World!"));
    }

    @Test
    @DisplayName("Authorisation Test With Role Admin needed")
    void authorisationWithTokenAndRoleTest() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN);
        var response = template.exchange("/api/greetings/notification", HttpMethod.GET, new HttpEntity<>(httpHeaders), GreetingInfo.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(403));
    }

}
