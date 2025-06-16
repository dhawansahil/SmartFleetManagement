package com.fleet.driver.client;

import com.fleet.driver.model.Driver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "n8n", url = "${n8n.webhook.url}")
public interface N8nClient {
    
    @PostMapping
    List<Driver> getAllDrivers(@RequestBody Map<String, Object> request);
    
    @PostMapping
    Driver getDriverById(@RequestBody Map<String, Object> request);
    
    @PostMapping
    Driver createDriver(@RequestBody Map<String, Object> request);
    
    @PostMapping
    Driver updateDriver(@RequestBody Map<String, Object> request);
    
    @PostMapping
    void deleteDriver(@RequestBody Map<String, Object> request);
    
    @PostMapping
    List<Driver> getDriversByStatus(@RequestBody Map<String, Object> request);
    
    @PostMapping
    List<Driver> getDriversWithExpiringLicenses(@RequestBody Map<String, Object> request);
    
    @PostMapping
    boolean isLicenseValid(@RequestBody Map<String, Object> request);
    
    @PostMapping
    List<Map<String, Object>> queryDrivers(@RequestBody Map<String, Object> request);
} 