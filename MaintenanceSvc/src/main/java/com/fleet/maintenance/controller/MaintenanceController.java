package com.fleet.maintenance.controller;

import com.fleet.maintenance.model.MaintenanceRecord;
import com.fleet.maintenance.model.MaintenanceStatus;
import com.fleet.maintenance.model.MaintenanceType;
import com.fleet.maintenance.service.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/maintenance")
@RequiredArgsConstructor
@Tag(name = "Maintenance Management", description = "APIs for managing vehicle maintenance records")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping
    @Operation(summary = "Create a new maintenance record", description = "Creates a new maintenance record in the system")
    public ResponseEntity<MaintenanceRecord> createMaintenanceRecord(@Valid @RequestBody MaintenanceRecord record) {
        return new ResponseEntity<>(maintenanceService.createMaintenanceRecord(record), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get maintenance record by ID", description = "Retrieves a maintenance record's details by its ID")
    public ResponseEntity<MaintenanceRecord> getMaintenanceRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceRecordById(id));
    }

    @GetMapping
    @Operation(summary = "Get all maintenance records", description = "Retrieves a list of all maintenance records in the system")
    public ResponseEntity<List<MaintenanceRecord>> getAllMaintenanceRecords() {
        return ResponseEntity.ok(maintenanceService.getAllMaintenanceRecords());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update maintenance record", description = "Updates an existing maintenance record's information")
    public ResponseEntity<MaintenanceRecord> updateMaintenanceRecord(
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceRecord record) {
        return ResponseEntity.ok(maintenanceService.updateMaintenanceRecord(id, record));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete maintenance record", description = "Removes a maintenance record from the system")
    public ResponseEntity<Void> deleteMaintenanceRecord(@PathVariable Long id) {
        maintenanceService.deleteMaintenanceRecord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get maintenance records by vehicle ID", description = "Retrieves all maintenance records for a specific vehicle")
    public ResponseEntity<List<MaintenanceRecord>> getMaintenanceRecordsByVehicleId(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceRecordsByVehicleId(vehicleId));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get maintenance records by type", description = "Retrieves all maintenance records of a specific type")
    public ResponseEntity<List<MaintenanceRecord>> getMaintenanceRecordsByType(@PathVariable MaintenanceType type) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceRecordsByType(type));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get maintenance records by status", description = "Retrieves all maintenance records with a specific status")
    public ResponseEntity<List<MaintenanceRecord>> getMaintenanceRecordsByStatus(@PathVariable MaintenanceStatus status) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceRecordsByStatus(status));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get maintenance records by date range", description = "Retrieves maintenance records within a specific date range")
    public ResponseEntity<List<MaintenanceRecord>> getMaintenanceRecordsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceRecordsByDateRange(startDate, endDate));
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming maintenance records", description = "Retrieves maintenance records that are due before a specific date")
    public ResponseEntity<List<MaintenanceRecord>> getUpcomingMaintenanceRecords(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(maintenanceService.getUpcomingMaintenanceRecords(date));
    }

    @GetMapping("/service-provider/{provider}")
    @Operation(summary = "Get maintenance records by service provider", description = "Retrieves all maintenance records from a specific service provider")
    public ResponseEntity<List<MaintenanceRecord>> getMaintenanceRecordsByServiceProvider(@PathVariable String provider) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceRecordsByServiceProvider(provider));
    }

    @GetMapping("/cost/vehicle/{vehicleId}")
    @Operation(summary = "Get total maintenance cost by vehicle", description = "Calculates the total maintenance cost for a specific vehicle")
    public ResponseEntity<Double> getTotalMaintenanceCostByVehicleId(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(maintenanceService.getTotalMaintenanceCostByVehicleId(vehicleId));
    }

    @GetMapping("/cost/date-range")
    @Operation(summary = "Get total maintenance cost by date range", description = "Calculates the total maintenance cost within a specific date range")
    public ResponseEntity<Double> getTotalMaintenanceCostByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(maintenanceService.getTotalMaintenanceCostByDateRange(startDate, endDate));
    }
} 