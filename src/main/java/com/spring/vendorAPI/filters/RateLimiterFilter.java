package com.spring.vendorAPI.filters;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import io.github.bucket4j.Bandwidth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

@Component
@WebFilter("/*")
public class RateLimiterFilter extends OncePerRequestFilter {

    private Bucket globalBucket;

    @Value("${rate.limiter.requests.per.second}")
    private int requestLimit;

    @Override
    public void afterPropertiesSet() {
        Bandwidth limit = Bandwidth.classic(requestLimit, Refill.intervally(requestLimit, Duration.ofSeconds(1)));
        globalBucket = Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        System.out.println("Remaining Tokens: " + globalBucket.getAvailableTokens() +
                ", Requests per second limit: " + requestLimit);

        ConsumptionProbe probe = globalBucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Rate limit exceeded. Try again later.\"}");
        }
    }
}
