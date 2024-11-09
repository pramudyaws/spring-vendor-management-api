package com.spring.vendorAPI.services.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.vendorAPI.entities.Vendor;
import com.spring.vendorAPI.repositories.VendorRepository;
import com.spring.vendorAPI.services.interfaces.VendorService;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public Vendor save(Vendor vendor) {
        if (vendorRepository.existsByName(vendor.getName())) {
            throw new IllegalArgumentException("Vendor name '" + vendor.getName() + "' has been taken");
        }
        return vendorRepository.save(vendor);
    }

    @Override
    public List<Vendor> findAll() {
        return vendorRepository.findAll();
    }

    @Override
    public Vendor findById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor with id " + id + " not found"));
    }

    @Override
    public Vendor updateById(Long id, Vendor vendorDetails) {
        Vendor existingVendor = this.findById(id);
        if (vendorRepository.existsByNameAndIdNot(vendorDetails.getName(), id)) {
            throw new IllegalArgumentException("Vendor name '" + vendorDetails.getName() + "' has been taken");
        }
        existingVendor.setName(vendorDetails.getName());
        return vendorRepository.save(existingVendor);
    }

    @Override
    public void deleteById(Long id) {
        Vendor existingVendor = this.findById(id);
        vendorRepository.delete(existingVendor);
    }
}
