package com.spring.vendorAPI.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class RateLimitTestRequestDto {

    @Min(1)
    @Max(50)
    private int requestCount;

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }
}
