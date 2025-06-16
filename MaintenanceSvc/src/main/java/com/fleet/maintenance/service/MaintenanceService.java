package com.fleet.maintenance.service;

import com.fleet.maintenance.model.MaintenanceRecord;
import com.fleet.maintenance.model.MaintenanceStatus;
import com.fleet.maintenance.model.MaintenanceType;
import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceService {
    
    MaintenanceRecord createMaintenanceRecord(MaintenanceRecord record);
    
    MaintenanceRecord getMaintenanceRecordById(Long id);
    
    List<MaintenanceRecord> getAllMaintenanceRecords();
    
    MaintenanceRecord updateMaintenanceRecord(Long id, MaintenanceRecord record);
    
    void deleteMaintenanceRecord(Long id);
    
    List<MaintenanceRecord> getMaintenanceRecordsByVehicleId(Long vehicleId);
    
    List<MaintenanceRecord> getMaintenanceRecordsByType(MaintenanceType type);
    
    List<MaintenanceRecord> getMaintenanceRecordsByStatus(MaintenanceStatus status);
    
    List<MaintenanceRecord> getMaintenanceRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<MaintenanceRecord> getUpcomingMaintenanceRecords(LocalDateTime date);
    
    List<MaintenanceRecord> getMaintenanceRecordsByServiceProvider(String serviceProvider);
    
    double getTotalMaintenanceCostByVehicleId(Long vehicleId);
    
    double getTotalMaintenanceCostByDateRange(LocalDateTime startDate, LocalDateTime endDate);
} 