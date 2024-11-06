package com.spring.vendorAPI.controllers;

import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public Map<String, String> helloWorld() {
        return Collections.singletonMap("message", "Welcome to Spring Vendor API!");
    }
}
