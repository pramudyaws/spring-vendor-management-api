package com.spring.vendorAPI.dtos;

import jakarta.validation.constraints.NotBlank;

public class CreateVendorDto {

    @NotBlank(message = "Vendor name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
