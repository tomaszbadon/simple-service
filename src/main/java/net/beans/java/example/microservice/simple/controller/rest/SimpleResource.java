package net.beans.java.example.microservice.simple.controller.rest;

import net.beans.java.example.microservice.simple.model.GreetingInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class SimpleResource {

    @GetMapping("/greetings/info")
    ResponseEntity<GreetingInfo> info(Principal principal, Authentication authentication) {
        return ResponseEntity.ok(new GreetingInfo("Hello World!"));
    }

    @GetMapping("/greetings/message")
    ResponseEntity<GreetingInfo> message(Principal principal, Authentication authentication) {
        return ResponseEntity.ok(new GreetingInfo("Hello World!"));
    }

    @GetMapping("/greetings/notification")
    ResponseEntity<GreetingInfo> notification(Principal principal, Authentication authentication) {
        return ResponseEntity.ok(new GreetingInfo("Hello World!"));
    }



}
