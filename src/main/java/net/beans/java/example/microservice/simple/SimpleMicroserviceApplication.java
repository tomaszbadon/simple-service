package net.beans.java.example.microservice.simple;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@Slf4j
public class SimpleMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleMicroserviceApplication.class, args);
	}

	@Bean
	CommandLineRunner run(JwtTokenRetrieverService tokenRetrieverService) {
		return (args) -> {

		};
	}

}

@RestController
class SimpleController {

	@GetMapping("/hello")
	ResponseEntity<String> hello() {
		return ResponseEntity.ok("Hello World");
	}

}
