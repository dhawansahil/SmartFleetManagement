package com.fleet.driver.service;

import com.fleet.driver.model.Driver;
import com.fleet.driver.model.DriverStatus;
import com.fleet.driver.repository.DriverRepository;
import com.fleet.driver.service.impl.DriverServiceImpl;
import com.fleet.driver.exception.DriverNotFoundException;
import com.fleet.driver.exception.DuplicateDriverException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverServiceImpl driverService;

    private Driver testDriver;

    @BeforeEach
    void setUp() {
        testDriver = new Driver();
        testDriver.setId(1L);
        testDriver.setFirstName("John");
        testDriver.setLastName("Doe");
        testDriver.setLicenseNumber("DL123456");
        testDriver.setLicenseExpiryDate(LocalDate.now().plusYears(1));
        testDriver.setEmail("john.doe@example.com");
        testDriver.setPhoneNumber("+1234567890");
        testDriver.setStatus(DriverStatus.ACTIVE);
    }

    @Test
    void createDriver_Success() {
        when(driverRepository.existsByLicenseNumber(anyString())).thenReturn(false);
        when(driverRepository.existsByEmail(anyString())).thenReturn(false);
        when(driverRepository.save(any(Driver.class))).thenReturn(testDriver);

        Driver result = driverService.createDriver(testDriver);

        assertNotNull(result);
        assertEquals(testDriver.getId(), result.getId());
        assertEquals(testDriver.getFirstName(), result.getFirstName());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void createDriver_DuplicateLicenseNumber() {
        when(driverRepository.existsByLicenseNumber(anyString())).thenReturn(true);

        assertThrows(DuplicateDriverException.class, () -> driverService.createDriver(testDriver));
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void createDriver_DuplicateEmail() {
        when(driverRepository.existsByLicenseNumber(anyString())).thenReturn(false);
        when(driverRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(DuplicateDriverException.class, () -> driverService.createDriver(testDriver));
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void getDriverById_Success() {
        when(driverRepository.findById(anyLong())).thenReturn(Optional.of(testDriver));

        Optional<Driver> result = driverService.getDriverById(1L);

        assertTrue(result.isPresent());
        assertEquals(testDriver.getId(), result.get().getId());
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    void getDriverById_NotFound() {
        when(driverRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Driver> result = driverService.getDriverById(1L);

        assertTrue(result.isEmpty());
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    void getAllDrivers_Success() {
        List<Driver> drivers = Arrays.asList(testDriver);
        when(driverRepository.findAll()).thenReturn(drivers);

        List<Driver> result = driverService.getAllDrivers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDriver.getId(), result.get(0).getId());
        verify(driverRepository, times(1)).findAll();
    }

    @Test
    void updateDriver_Success() {
        when(driverRepository.findById(anyLong())).thenReturn(Optional.of(testDriver));
        when(driverRepository.existsByLicenseNumber(anyString())).thenReturn(false);
        when(driverRepository.existsByEmail(anyString())).thenReturn(false);
        when(driverRepository.save(any(Driver.class))).thenReturn(testDriver);

        Driver result = driverService.updateDriver(1L, testDriver);

        assertNotNull(result);
        assertEquals(testDriver.getId(), result.getId());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void updateDriver_NotFound() {
        when(driverRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DriverNotFoundException.class, () -> driverService.updateDriver(1L, testDriver));
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void deleteDriver_Success() {
        when(driverRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(driverRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> driverService.deleteDriver(1L));
        verify(driverRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDriver_NotFound() {
        when(driverRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(DriverNotFoundException.class, () -> driverService.deleteDriver(1L));
        verify(driverRepository, never()).deleteById(anyLong());
    }

    @Test
    void getDriversByStatus_Success() {
        List<Driver> drivers = Arrays.asList(testDriver);
        when(driverRepository.findByStatus(any(DriverStatus.class))).thenReturn(drivers);

        List<Driver> result = driverService.getDriversByStatus("ACTIVE");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(DriverStatus.ACTIVE, result.get(0).getStatus());
        verify(driverRepository, times(1)).findByStatus(DriverStatus.ACTIVE);
    }

    @Test
    void getDriversWithExpiringLicenses_Success() {
        List<Driver> drivers = Arrays.asList(testDriver);
        when(driverRepository.findByLicenseExpiryDateBefore(any(LocalDate.class))).thenReturn(drivers);

        List<Driver> result = driverService.getDriversWithExpiringLicenses(30);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(driverRepository, times(1)).findByLicenseExpiryDateBefore(any(LocalDate.class));
    }

    @Test
    void isLicenseValid_Valid() {
        when(driverRepository.findByLicenseNumber(anyString())).thenReturn(Optional.of(testDriver));

        boolean result = driverService.isLicenseValid("DL123456");

        assertTrue(result);
        verify(driverRepository, times(1)).findByLicenseNumber("DL123456");
    }

    @Test
    void isLicenseValid_Invalid() {
        when(driverRepository.findByLicenseNumber(anyString())).thenReturn(Optional.empty());

        boolean result = driverService.isLicenseValid("DL123456");

        assertFalse(result);
        verify(driverRepository, times(1)).findByLicenseNumber("DL123456");
    }
} 