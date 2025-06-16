package com.fleet.driver.service.impl;

import com.fleet.driver.model.Driver;
import com.fleet.driver.model.DriverStatus;
import com.fleet.driver.service.DriverService;
import com.fleet.driver.exception.DriverNotFoundException;
import com.fleet.driver.exception.DuplicateDriverException;
import com.fleet.driver.client.N8nClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverServiceImpl implements DriverService {

    private final N8nClient n8nClient;

    @Autowired
    public DriverServiceImpl(N8nClient n8nClient) {
        this.n8nClient = n8nClient;
    }

    @Override
    public List<Driver> getAllDrivers() {
        Map<String, Object> request = new HashMap<>();
        request.put("operation", "getAll");
        return n8nClient.getAllDrivers(request);
    }

    @Override
    public Driver getDriverById(Long id) {
        Map<String, Object> request = new HashMap<>();
        request.put("operation", "getById");
        request.put("id", id);
        
        Driver driver = n8nClient.getDriverById(request);
        if (driver == null) {
            throw new DriverNotFoundException("Driver not found");
        }
        return driver;
    }

    @Override
    @Transactional
    public Driver createDriver(Driver driver) {
        Map<String, Object> request = new HashMap<>();
        request.put("operation", "create");
        request.put("driver", driver);
        return n8nClient.createDriver(request);
    }

    @Override
    @Transactional
    public Driver updateDriver(Long id, Driver driver) {
        Map<String, Object> request = new HashMap<>();
        request.put("operation", "update");
        request.put("id", id);
        request.put("driver", driver);
        
        Driver updatedDriver = n8nClient.updateDriver(request);
        if (updatedDriver == null) {
            throw new DriverNotFoundException("Driver not found");
        }
        return updatedDriver;
    }

    @Override
    @Transactional
    public void deleteDriver(Long id) {
        Map<String, Object> request = new HashMap<>();
        request.put("operation", "delete");
        request.put("id", id);
        n8nClient.deleteDriver(request);
    }

    @Override
    public List<Driver> getDriversByStatus(String status) {
        Map<String, Object> request = new HashMap<>();
        request.put("operation", "getByStatus");
        request.put("status", status);
        return n8nClient.getDriversByStatus(request);
    }

    @Override
    public List<Driver> getDriversWithExpiringLicenses(int daysThreshold) {
        Map<String, Object> request = new HashMap<>();
        request.put("operation", "getExpiringLicenses");
        request.put("daysThreshold", daysThreshold);
        return n8nClient.getDriversWithExpiringLicenses(request);
    }

    @Override
    public boolean isLicenseValid(String licenseNumber) {
        Map<String, Object> request = new HashMap<>();
        request.put("operation", "checkLicense");
        request.put("licenseNumber", licenseNumber);
        return n8nClient.isLicenseValid(request);
    }

    @Override
    public List<Map<String, Object>> queryDrivers(Map<String, Object> queryParams) {
        Map<String, Object> request = new HashMap<>();
        request.put("operation", "query");
        request.put("queryParams", queryParams);
        return n8nClient.queryDrivers(request);
    }
} 