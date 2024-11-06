package com.spring.vendorAPI.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.vendorAPI.entities.Vendor;
import com.spring.vendorAPI.services.interfaces.VendorService;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createVendor(@RequestBody Vendor vendor) {
        try {
            Vendor savedVendor = vendorService.save(vendor);
            return ResponseEntity.ok(Collections.singletonMap("data", savedVendor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllVendors() {
        List<Vendor> vendors = vendorService.findAll();
        return ResponseEntity.ok(Collections.singletonMap("data", vendors));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getVendorById(@PathVariable Long id) {
        try {
            Vendor vendor = vendorService.findById(id);
            return ResponseEntity.ok(Collections.singletonMap("data", vendor));
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = Collections.singletonMap("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateVendor(@PathVariable Long id, @RequestBody Vendor vendorDetails) {
        try {
            Vendor updatedVendor = vendorService.updateById(id, vendorDetails);
            return ResponseEntity.ok(Collections.singletonMap("data", updatedVendor));
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = Collections.singletonMap("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteVendor(@PathVariable Long id) {
        try {
            vendorService.deleteById(id);
            Map<String, String> response = Collections.singletonMap("message", "Vendor with id " + id + " has been deleted");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = Collections.singletonMap("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
