package com.spring.vendorAPI.services.interfaces;

import java.util.List;

import com.spring.vendorAPI.entities.Vendor;

public interface VendorService {
    Vendor save(Vendor vendor);
    List<Vendor> findAll();
    Vendor findById(Long id);
    Vendor updateById(Long id, Vendor vendorDetails);
    void deleteById(Long id);
}
