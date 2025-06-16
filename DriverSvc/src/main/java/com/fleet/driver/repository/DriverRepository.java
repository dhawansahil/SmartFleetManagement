package com.fleet.driver.repository;

import com.fleet.driver.model.Driver;
import com.fleet.driver.model.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByLicenseNumber(String licenseNumber);
    boolean existsByEmail(String email);
    Optional<Driver> findByLicenseNumber(String licenseNumber);
    List<Driver> findByStatus(DriverStatus status);
    List<Driver> findByLicenseExpiryDateBefore(LocalDate date);
} 