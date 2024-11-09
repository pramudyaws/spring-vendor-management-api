package com.spring.vendorAPI.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class UpdateVendorDto {

    @NotBlank(message = "Vendor name is required")
    @Schema(description = "Vendor name", example = "Vendor 1 updated")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
