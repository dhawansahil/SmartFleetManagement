package com.fleet.vehicle.service;

import com.fleet.vehicle.model.Vehicle;
import com.fleet.vehicle.model.VehicleStatus;
import com.fleet.vehicle.model.VehicleType;
import com.fleet.vehicle.repository.VehicleRepository;
import com.fleet.vehicle.service.impl.VehicleServiceImpl;
import com.fleet.vehicle.exception.VehicleNotFoundException;
import com.fleet.vehicle.exception.DuplicateVehicleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle testVehicle;
    private List<Vehicle> testVehicles;

    @BeforeEach
    void setUp() {
        testVehicle = new Vehicle();
        testVehicle.setId(1L);
        testVehicle.setRegistrationNumber("ABC123");
        testVehicle.setMake("Toyota");
        testVehicle.setModel("Camry");
        testVehicle.setYear(2022);
        testVehicle.setVehicleType(VehicleType.SEDAN);
        testVehicle.setStatus(VehicleStatus.ACTIVE);
        testVehicle.setMileage(10000);
        testVehicle.setLastServiceDate(LocalDate.now().minusMonths(3));
        testVehicle.setNextServiceDate(LocalDate.now().plusMonths(3));

        testVehicles = Arrays.asList(testVehicle);
    }

    @Test
    void createVehicle_Success() {
        when(vehicleRepository.findByRegistrationNumber(anyString())).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);

        Vehicle createdVehicle = vehicleService.createVehicle(testVehicle);

        assertNotNull(createdVehicle);
        assertEquals("ABC123", createdVehicle.getRegistrationNumber());
        assertEquals("Toyota", createdVehicle.getMake());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void createVehicle_DuplicateRegistration() {
        when(vehicleRepository.findByRegistrationNumber(anyString())).thenReturn(Optional.of(testVehicle));

        assertThrows(DuplicateVehicleException.class, () -> vehicleService.createVehicle(testVehicle));
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void getVehicleById_Success() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(testVehicle));

        Optional<Vehicle> foundVehicle = vehicleService.getVehicleById(1L);

        assertTrue(foundVehicle.isPresent());
        assertEquals(1L, foundVehicle.get().getId());
        verify(vehicleRepository, times(1)).findById(1L);
    }

    @Test
    void getVehicleById_NotFound() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Vehicle> foundVehicle = vehicleService.getVehicleById(1L);

        assertTrue(foundVehicle.isEmpty());
        verify(vehicleRepository, times(1)).findById(1L);
    }

    @Test
    void getAllVehicles_Success() {
        when(vehicleRepository.findAll()).thenReturn(testVehicles);

        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void updateVehicle_Success() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(testVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);

        Vehicle updatedVehicle = vehicleService.updateVehicle(1L, testVehicle);

        assertNotNull(updatedVehicle);
        assertEquals(1L, updatedVehicle.getId());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void updateVehicle_NotFound() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(VehicleNotFoundException.class, () -> vehicleService.updateVehicle(1L, testVehicle));
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void deleteVehicle_Success() {
        when(vehicleRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(vehicleRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> vehicleService.deleteVehicle(1L));
        verify(vehicleRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteVehicle_NotFound() {
        when(vehicleRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(VehicleNotFoundException.class, () -> vehicleService.deleteVehicle(1L));
        verify(vehicleRepository, never()).deleteById(anyLong());
    }

    @Test
    void getVehiclesByType_Success() {
        when(vehicleRepository.findByVehicleType(any(VehicleType.class))).thenReturn(testVehicles);

        List<Vehicle> vehicles = vehicleService.getVehiclesByType(VehicleType.SEDAN);

        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
        assertEquals(VehicleType.SEDAN, vehicles.get(0).getVehicleType());
        verify(vehicleRepository, times(1)).findByVehicleType(VehicleType.SEDAN);
    }

    @Test
    void getVehiclesByStatus_Success() {
        when(vehicleRepository.findByStatus(any(VehicleStatus.class))).thenReturn(testVehicles);

        List<Vehicle> vehicles = vehicleService.getVehiclesByStatus(VehicleStatus.ACTIVE);

        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
        assertEquals(VehicleStatus.ACTIVE, vehicles.get(0).getStatus());
        verify(vehicleRepository, times(1)).findByStatus(VehicleStatus.ACTIVE);
    }

    @Test
    void getVehiclesNeedingService_Success() {
        when(vehicleRepository.findByNextServiceDateBefore(any(LocalDate.class))).thenReturn(testVehicles);

        List<Vehicle> vehicles = vehicleService.getVehiclesNeedingService(30);

        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
        verify(vehicleRepository, times(1)).findByNextServiceDateBefore(any(LocalDate.class));
    }

    @Test
    void getVehicleByRegistrationNumber_Success() {
        when(vehicleRepository.findByRegistrationNumber(anyString())).thenReturn(Optional.of(testVehicle));

        Optional<Vehicle> foundVehicle = vehicleService.getVehicleByRegistrationNumber("ABC123");

        assertTrue(foundVehicle.isPresent());
        assertEquals("ABC123", foundVehicle.get().getRegistrationNumber());
        verify(vehicleRepository, times(1)).findByRegistrationNumber("ABC123");
    }
} 