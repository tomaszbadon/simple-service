package net.beans.java.example.microservice.simple.controller.rest;

import lombok.extern.slf4j.Slf4j;
import net.beans.java.example.microservice.simple.data.model.dto.category.CategoriesInfo;
import net.beans.java.example.microservice.simple.data.model.dto.category.CategoryInfo;
import net.beans.java.example.microservice.simple.data.model.jpa.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Slf4j
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryResourceIntegrationTest extends SimpleMicroserviceResourceBaseTest {

    @Autowired
    private TestRestTemplate template;

    @Test
    @DisplayName("Authorisation Test Without Token")
    void getAllCategories() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN);
        var response = template.exchange("/v1/categories/", GET, new HttpEntity<>(null, headers), CategoriesInfo.class);
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(response).extracting(ResponseEntity::getBody).extracting(CategoriesInfo::getCategories).asInstanceOf(LIST).size().isGreaterThan(0);
    }

    @Test
    @DisplayName("Authorisation Test Without Token")
    void createNewCategory() {
        CategoryInfo categoryInfo = new CategoryInfo(null, "Sex Toys", "Description", null, null, Set.of());
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN);
        var response = template.exchange("/v1/categories/", POST, new HttpEntity<>(categoryInfo, headers), Category.class);
        assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.CREATED.value());
    }

}
