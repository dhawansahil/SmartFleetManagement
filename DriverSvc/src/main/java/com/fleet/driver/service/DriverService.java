package com.fleet.driver.service;

import com.fleet.driver.model.Driver;
import com.fleet.driver.model.DriverStatus;
import com.fleet.driver.repository.DriverRepository;
import com.fleet.driver.exception.DriverNotFoundException;
import com.fleet.driver.exception.DuplicateDriverException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DriverService {
    List<Driver> getAllDrivers();
    Driver getDriverById(Long id);
    Driver createDriver(Driver driver);
    Driver updateDriver(Long id, Driver driver);
    void deleteDriver(Long id);
    List<Driver> getDriversByStatus(String status);
    List<Driver> getDriversWithExpiringLicenses(int daysThreshold);
    boolean isLicenseValid(String licenseNumber);
    List<Map<String, Object>> queryDrivers(Map<String, Object> queryParams);
} 