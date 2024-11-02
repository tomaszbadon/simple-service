package net.beans.java.example.microservice.simple;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.extern.slf4j.Slf4j;
import net.beans.java.example.microservice.simple.data.model.jpa.Category;
import net.beans.java.example.microservice.simple.exception.EntityNotFoundException;
import net.beans.java.example.microservice.simple.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.UUID;

@Slf4j
@TestConfiguration(proxyBeanMethods = false)
public class TestSimpleMicroserviceApplication {

    @Bean
    @ServiceConnection
    public MySQLContainer mySQLContainer() {
        var mysql = new MySQLContainer<>(DockerImageName.parse("mysql:9.1.0"))
                .withDatabaseName("simple-service")
                .withUsername("test-user")
                .withPassword("test");
        return mysql;
    }

    @Bean
    public KeycloakContainer keycloakContainer(DynamicPropertyRegistry registry) {
        var keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:24.0.2")
                .withEnv("DB_VENDOR", "h2")
                .withAdminUsername("admin")
                .withAdminPassword("admin")
                .withContextPath("/auth")
                .withRealmImportFile("/simple-application-realm.json");

        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/simple-application-realm");
        registry.add("spring.security.oauth2.client.provider.keycloak.token-uri", () -> keycloak.getAuthServerUrl() + "/realms/simple-application-realm/protocol/openid-connect/token");
        return keycloak;
    }

    @Bean
    public CommandLineRunner commandLineRunner(CategoryRepository categoryRepository) {
        return (args) -> {
            List<Category> categories = categoryRepository.findByName("Electronics");
            log.info("Number of subcategories is: " + categories.get(0).getSubcategories().size());

            categories = categoryRepository.findSubcategoriesRecursively("Electronics");
            log.info("Number of subcategories is: " + categories.size());
            categories.forEach(category -> log.info("Category: " + category.getName()));

            try {
                Category category = categoryRepository.getById(UUID.randomUUID());
                log.info(category.getName());
            } catch (EntityNotFoundException ex) {
                log.error(ex.getMessage(), ex);
            }

        };
    }

    public static void main(String[] args) {
        SpringApplication.from(SimpleMicroserviceApplication::main).with(TestSimpleMicroserviceApplication.class).run(args);
    }

}
