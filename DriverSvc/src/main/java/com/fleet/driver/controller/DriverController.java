package com.fleet.driver.controller;

import com.fleet.driver.model.Driver;
import com.fleet.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/SmartFleet/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @PostMapping
    public ResponseEntity<Driver> createDriver(@RequestBody Driver driver) {
        return ResponseEntity.ok(driverService.createDriver(driver));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Driver> updateDriver(@PathVariable Long id, @RequestBody Driver driver) {
        return ResponseEntity.ok(driverService.updateDriver(id, driver));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.ok().build();
    }

    // Endpoint for n8n to fetch driver data with custom query
    @PostMapping("/query")
    public ResponseEntity<List<Map<String, Object>>> queryDrivers(@RequestBody Map<String, Object> queryParams) {
        return ResponseEntity.ok(driverService.queryDrivers(queryParams));
    }
} 