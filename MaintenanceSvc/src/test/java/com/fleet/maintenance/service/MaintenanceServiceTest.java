package com.fleet.maintenance.service;

import com.fleet.maintenance.model.MaintenanceRecord;
import com.fleet.maintenance.model.MaintenanceStatus;
import com.fleet.maintenance.model.MaintenanceType;
import com.fleet.maintenance.repository.MaintenanceRepository;
import com.fleet.maintenance.service.impl.MaintenanceServiceImpl;
import com.fleet.maintenance.exception.MaintenanceRecordNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceTest {

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @InjectMocks
    private MaintenanceServiceImpl maintenanceService;

    private MaintenanceRecord testRecord;

    @BeforeEach
    void setUp() {
        testRecord = new MaintenanceRecord();
        testRecord.setId(1L);
        testRecord.setVehicleId(1L);
        testRecord.setMaintenanceType(MaintenanceType.ROUTINE_SERVICE);
        testRecord.setDescription("Regular maintenance check");
        testRecord.setServiceDate(LocalDateTime.now());
        testRecord.setNextServiceDate(LocalDateTime.now().plusMonths(6));
        testRecord.setCost(new BigDecimal("500.00"));
        testRecord.setServiceProvider("Auto Service Center");
        testRecord.setMileage(50000);
        testRecord.setStatus(MaintenanceStatus.COMPLETED);
    }

    @Test
    void createMaintenanceRecord_Success() {
        when(maintenanceRepository.save(any(MaintenanceRecord.class))).thenReturn(testRecord);

        MaintenanceRecord result = maintenanceService.createMaintenanceRecord(testRecord);

        assertNotNull(result);
        assertEquals(testRecord.getId(), result.getId());
        assertEquals(testRecord.getMaintenanceType(), result.getMaintenanceType());
        verify(maintenanceRepository, times(1)).save(any(MaintenanceRecord.class));
    }

    @Test
    void getMaintenanceRecordById_Success() {
        when(maintenanceRepository.findById(anyLong())).thenReturn(java.util.Optional.of(testRecord));

        MaintenanceRecord result = maintenanceService.getMaintenanceRecordById(1L);

        assertNotNull(result);
        assertEquals(testRecord.getId(), result.getId());
        verify(maintenanceRepository, times(1)).findById(1L);
    }

    @Test
    void getMaintenanceRecordById_NotFound() {
        when(maintenanceRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(MaintenanceRecordNotFoundException.class, () -> maintenanceService.getMaintenanceRecordById(1L));
        verify(maintenanceRepository, times(1)).findById(1L);
    }

    @Test
    void getAllMaintenanceRecords_Success() {
        List<MaintenanceRecord> records = Arrays.asList(testRecord);
        when(maintenanceRepository.findAll()).thenReturn(records);

        List<MaintenanceRecord> result = maintenanceService.getAllMaintenanceRecords();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testRecord.getId(), result.get(0).getId());
        verify(maintenanceRepository, times(1)).findAll();
    }

    @Test
    void updateMaintenanceRecord_Success() {
        when(maintenanceRepository.findById(anyLong())).thenReturn(java.util.Optional.of(testRecord));
        when(maintenanceRepository.save(any(MaintenanceRecord.class))).thenReturn(testRecord);

        MaintenanceRecord result = maintenanceService.updateMaintenanceRecord(1L, testRecord);

        assertNotNull(result);
        assertEquals(testRecord.getId(), result.getId());
        verify(maintenanceRepository, times(1)).save(any(MaintenanceRecord.class));
    }

    @Test
    void updateMaintenanceRecord_NotFound() {
        when(maintenanceRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(MaintenanceRecordNotFoundException.class, () -> maintenanceService.updateMaintenanceRecord(1L, testRecord));
        verify(maintenanceRepository, never()).save(any(MaintenanceRecord.class));
    }

    @Test
    void deleteMaintenanceRecord_Success() {
        when(maintenanceRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(maintenanceRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> maintenanceService.deleteMaintenanceRecord(1L));
        verify(maintenanceRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMaintenanceRecord_NotFound() {
        when(maintenanceRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(MaintenanceRecordNotFoundException.class, () -> maintenanceService.deleteMaintenanceRecord(1L));
        verify(maintenanceRepository, never()).deleteById(anyLong());
    }

    @Test
    void getMaintenanceRecordsByVehicleId_Success() {
        List<MaintenanceRecord> records = Arrays.asList(testRecord);
        when(maintenanceRepository.findByVehicleId(anyLong())).thenReturn(records);

        List<MaintenanceRecord> result = maintenanceService.getMaintenanceRecordsByVehicleId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testRecord.getVehicleId(), result.get(0).getVehicleId());
        verify(maintenanceRepository, times(1)).findByVehicleId(1L);
    }

    @Test
    void getMaintenanceRecordsByType_Success() {
        List<MaintenanceRecord> records = Arrays.asList(testRecord);
        when(maintenanceRepository.findByMaintenanceType(any(MaintenanceType.class))).thenReturn(records);

        List<MaintenanceRecord> result = maintenanceService.getMaintenanceRecordsByType(MaintenanceType.ROUTINE_SERVICE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(MaintenanceType.ROUTINE_SERVICE, result.get(0).getMaintenanceType());
        verify(maintenanceRepository, times(1)).findByMaintenanceType(MaintenanceType.ROUTINE_SERVICE);
    }

    @Test
    void getMaintenanceRecordsByStatus_Success() {
        List<MaintenanceRecord> records = Arrays.asList(testRecord);
        when(maintenanceRepository.findByStatus(any(MaintenanceStatus.class))).thenReturn(records);

        List<MaintenanceRecord> result = maintenanceService.getMaintenanceRecordsByStatus(MaintenanceStatus.COMPLETED);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(MaintenanceStatus.COMPLETED, result.get(0).getStatus());
        verify(maintenanceRepository, times(1)).findByStatus(MaintenanceStatus.COMPLETED);
    }

    @Test
    void getMaintenanceRecordsByDateRange_Success() {
        List<MaintenanceRecord> records = Arrays.asList(testRecord);
        when(maintenanceRepository.findByServiceDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(records);

        List<MaintenanceRecord> result = maintenanceService.getMaintenanceRecordsByDateRange(
                LocalDateTime.now(), LocalDateTime.now().plusMonths(1));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(maintenanceRepository, times(1)).findByServiceDateBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getUpcomingMaintenanceRecords_Success() {
        List<MaintenanceRecord> records = Arrays.asList(testRecord);
        when(maintenanceRepository.findByNextServiceDateBefore(any(LocalDateTime.class))).thenReturn(records);

        List<MaintenanceRecord> result = maintenanceService.getUpcomingMaintenanceRecords(LocalDateTime.now().plusMonths(1));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(maintenanceRepository, times(1)).findByNextServiceDateBefore(any(LocalDateTime.class));
    }

    @Test
    void getMaintenanceRecordsByServiceProvider_Success() {
        List<MaintenanceRecord> records = Arrays.asList(testRecord);
        when(maintenanceRepository.findByServiceProvider(anyString())).thenReturn(records);

        List<MaintenanceRecord> result = maintenanceService.getMaintenanceRecordsByServiceProvider("Auto Service Center");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Auto Service Center", result.get(0).getServiceProvider());
        verify(maintenanceRepository, times(1)).findByServiceProvider("Auto Service Center");
    }

    @Test
    void getTotalMaintenanceCostByVehicleId_Success() {
        List<MaintenanceRecord> records = Arrays.asList(testRecord);
        when(maintenanceRepository.findByVehicleId(anyLong())).thenReturn(records);

        double result = maintenanceService.getTotalMaintenanceCostByVehicleId(1L);

        assertEquals(500.0, result);
        verify(maintenanceRepository, times(1)).findByVehicleId(1L);
    }

    @Test
    void getTotalMaintenanceCostByDateRange_Success() {
        List<MaintenanceRecord> records = Arrays.asList(testRecord);
        when(maintenanceRepository.findByServiceDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(records);

        double result = maintenanceService.getTotalMaintenanceCostByDateRange(
                LocalDateTime.now(), LocalDateTime.now().plusMonths(1));

        assertEquals(500.0, result);
        verify(maintenanceRepository, times(1)).findByServiceDateBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }
} 