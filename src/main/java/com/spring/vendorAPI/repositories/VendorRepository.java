package com.spring.vendorAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.vendorAPI.entities.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
