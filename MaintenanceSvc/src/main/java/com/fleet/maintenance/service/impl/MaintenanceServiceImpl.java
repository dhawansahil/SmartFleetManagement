package com.fleet.maintenance.service.impl;

import com.fleet.maintenance.model.MaintenanceRecord;
import com.fleet.maintenance.model.MaintenanceStatus;
import com.fleet.maintenance.model.MaintenanceType;
import com.fleet.maintenance.repository.MaintenanceRepository;
import com.fleet.maintenance.service.MaintenanceService;
import com.fleet.maintenance.exception.MaintenanceRecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    @Override
    public MaintenanceRecord createMaintenanceRecord(MaintenanceRecord record) {
        return maintenanceRepository.save(record);
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceRecord getMaintenanceRecordById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new MaintenanceRecordNotFoundException("Maintenance record not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        return maintenanceRepository.findAll();
    }

    @Override
    public MaintenanceRecord updateMaintenanceRecord(Long id, MaintenanceRecord record) {
        MaintenanceRecord existingRecord = maintenanceRepository.findById(id)
                .orElseThrow(() -> new MaintenanceRecordNotFoundException("Maintenance record not found with id: " + id));
        
        existingRecord.setVehicleId(record.getVehicleId());
        existingRecord.setMaintenanceType(record.getMaintenanceType());
        existingRecord.setDescription(record.getDescription());
        existingRecord.setServiceDate(record.getServiceDate());
        existingRecord.setNextServiceDate(record.getNextServiceDate());
        existingRecord.setCost(record.getCost());
        existingRecord.setServiceProvider(record.getServiceProvider());
        existingRecord.setMileage(record.getMileage());
        existingRecord.setStatus(record.getStatus());
        
        return maintenanceRepository.save(existingRecord);
    }

    @Override
    public void deleteMaintenanceRecord(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new MaintenanceRecordNotFoundException("Maintenance record not found with id: " + id);
        }
        maintenanceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecord> getMaintenanceRecordsByVehicleId(Long vehicleId) {
        return maintenanceRepository.findByVehicleId(vehicleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecord> getMaintenanceRecordsByType(MaintenanceType type) {
        return maintenanceRepository.findByMaintenanceType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecord> getMaintenanceRecordsByStatus(MaintenanceStatus status) {
        return maintenanceRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecord> getMaintenanceRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return maintenanceRepository.findByServiceDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecord> getUpcomingMaintenanceRecords(LocalDateTime date) {
        return maintenanceRepository.findByNextServiceDateBefore(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRecord> getMaintenanceRecordsByServiceProvider(String serviceProvider) {
        return maintenanceRepository.findByServiceProvider(serviceProvider);
    }

    @Override
    @Transactional(readOnly = true)
    public double getTotalMaintenanceCostByVehicleId(Long vehicleId) {
        return maintenanceRepository.findByVehicleId(vehicleId).stream()
                .mapToDouble(record -> record.getCost().doubleValue())
                .sum();
    }

    @Override
    @Transactional(readOnly = true)
    public double getTotalMaintenanceCostByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return maintenanceRepository.findByServiceDateBetween(startDate, endDate).stream()
                .mapToDouble(record -> record.getCost().doubleValue())
                .sum();
    }
} 