package com.fleet.maintenance.repository;

import com.fleet.maintenance.model.MaintenanceRecord;
import com.fleet.maintenance.model.MaintenanceStatus;
import com.fleet.maintenance.model.MaintenanceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord, Long> {
    
    List<MaintenanceRecord> findByVehicleId(Long vehicleId);
    
    List<MaintenanceRecord> findByMaintenanceType(MaintenanceType type);
    
    List<MaintenanceRecord> findByStatus(MaintenanceStatus status);
    
    List<MaintenanceRecord> findByServiceDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<MaintenanceRecord> findByNextServiceDateBefore(LocalDateTime date);
    
    List<MaintenanceRecord> findByVehicleIdAndStatus(Long vehicleId, MaintenanceStatus status);
    
    List<MaintenanceRecord> findByServiceProvider(String serviceProvider);
} 