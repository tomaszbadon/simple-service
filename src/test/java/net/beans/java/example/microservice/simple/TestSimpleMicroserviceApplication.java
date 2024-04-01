package net.beans.java.example.microservice.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestSimpleMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(SimpleMicroserviceApplication::main).with(TestSimpleMicroserviceApplication.class).run(args);
	}

}