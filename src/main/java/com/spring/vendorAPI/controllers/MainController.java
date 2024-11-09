package com.spring.vendorAPI.controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.vendorAPI.dtos.RateLimitTestRequestDto;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class MainController {

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/")
    public Map<String, String> helloWorld() {
        return Collections.singletonMap("message", "Welcome to Spring Vendor API!");
    }

    @PostMapping("/rate-limit-test")
    public ResponseEntity<Map<String, String>> rateLimitTest(@RequestBody RateLimitTestRequestDto requestBody)
            throws InterruptedException, ExecutionException {

        final int MAX_THREADS = 50;
        int requestCount = requestBody.getRequestCount();

        if (requestCount > MAX_THREADS) {
            String errorMsg = "requestCount must be less than or equal to " + MAX_THREADS;
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", errorMsg);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        String baseUrl = request.getRequestURL().toString();
        String finalBaseUrl = baseUrl.substring(0, baseUrl.length() - request.getRequestURI().length());

        ExecutorService executor = Executors.newFixedThreadPool(requestCount);
        int successCount = 0;
        int failureCount = 0;

        Callable<Boolean> task = () -> {
            try {
                URL url = new URL(finalBaseUrl + "/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                return responseCode == 200;
            } catch (IOException e) {
                return false;
            }
        };

        for (Future<Boolean> result : executor.invokeAll(Collections.nCopies(requestCount, task))) {
            if (result.get()) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        executor.shutdown();

        // Calling rateLimitTest should not affect success/failure count.
        // Therefore, successCount += 1 and failureCount -= 1
        // to calculate only API calls on index/welcome url
        successCount++;
        failureCount--;

        Map<String, String> response = new HashMap<>();
        response.put("successCount", String.valueOf(successCount));
        response.put("failureCount", String.valueOf(failureCount));

        return ResponseEntity.ok(response);
    }
}
