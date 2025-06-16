package com.fleet.driver.controller;

import com.fleet.driver.model.Driver;
import com.fleet.driver.model.DriverStatus;
import com.fleet.driver.service.DriverService;
import com.fleet.driver.exception.DriverNotFoundException;
import com.fleet.driver.exception.DuplicateDriverException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DriverController.class)
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverService driverService;

    @Autowired
    private ObjectMapper objectMapper;

    private Driver testDriver;
    private List<Driver> testDrivers;

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

        testDrivers = Arrays.asList(testDriver);
    }

    @Test
    void createDriver_Success() throws Exception {
        when(driverService.createDriver(any(Driver.class))).thenReturn(testDriver);

        mockMvc.perform(post("/api/v1/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDriver)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(driverService, times(1)).createDriver(any(Driver.class));
    }

    @Test
    void createDriver_DuplicateDriver() throws Exception {
        when(driverService.createDriver(any(Driver.class)))
                .thenThrow(new DuplicateDriverException("Driver already exists"));

        mockMvc.perform(post("/api/v1/drivers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDriver)))
                .andExpect(status().isBadRequest());

        verify(driverService, times(1)).createDriver(any(Driver.class));
    }

    @Test
    void getDriverById_Success() throws Exception {
        when(driverService.getDriverById(anyLong())).thenReturn(testDriver);

        mockMvc.perform(get("/api/v1/drivers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(driverService, times(1)).getDriverById(1L);
    }

    @Test
    void getDriverById_NotFound() throws Exception {
        when(driverService.getDriverById(anyLong()))
            .thenThrow(new DriverNotFoundException("Driver not found"));

        mockMvc.perform(get("/api/v1/drivers/1"))
                .andExpect(status().isNotFound());

        verify(driverService, times(1)).getDriverById(1L);
    }

    @Test
    void getAllDrivers_Success() throws Exception {
        when(driverService.getAllDrivers()).thenReturn(testDrivers);

        mockMvc.perform(get("/api/v1/drivers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));

        verify(driverService, times(1)).getAllDrivers();
    }

    @Test
    void updateDriver_Success() throws Exception {
        when(driverService.updateDriver(anyLong(), any(Driver.class))).thenReturn(testDriver);

        mockMvc.perform(put("/api/v1/drivers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDriver)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(driverService, times(1)).updateDriver(eq(1L), any(Driver.class));
    }

    @Test
    void updateDriver_NotFound() throws Exception {
        when(driverService.updateDriver(anyLong(), any(Driver.class)))
                .thenThrow(new DriverNotFoundException("Driver not found"));

        mockMvc.perform(put("/api/v1/drivers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDriver)))
                .andExpect(status().isNotFound());

        verify(driverService, times(1)).updateDriver(eq(1L), any(Driver.class));
    }

    @Test
    void deleteDriver_Success() throws Exception {
        doNothing().when(driverService).deleteDriver(anyLong());

        mockMvc.perform(delete("/api/v1/drivers/1"))
                .andExpect(status().isNoContent());

        verify(driverService, times(1)).deleteDriver(1L);
    }

    @Test
    void deleteDriver_NotFound() throws Exception {
        doThrow(new DriverNotFoundException("Driver not found"))
                .when(driverService).deleteDriver(anyLong());

        mockMvc.perform(delete("/api/v1/drivers/1"))
                .andExpect(status().isNotFound());

        verify(driverService, times(1)).deleteDriver(1L);
    }

    @Test
    void getDriversByStatus_Success() throws Exception {
        when(driverService.getDriversByStatus(anyString())).thenReturn(testDrivers);

        mockMvc.perform(get("/api/v1/drivers/status/ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));

        verify(driverService, times(1)).getDriversByStatus("ACTIVE");
    }

    @Test
    void getDriversWithExpiringLicenses_Success() throws Exception {
        when(driverService.getDriversWithExpiringLicenses(anyInt())).thenReturn(testDrivers);

        mockMvc.perform(get("/api/v1/drivers/expiring-licenses")
                .param("daysThreshold", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(driverService, times(1)).getDriversWithExpiringLicenses(30);
    }

    @Test
    void validateLicense_Success() throws Exception {
        when(driverService.isLicenseValid(anyString())).thenReturn(true);

        mockMvc.perform(get("/api/v1/drivers/validate-license/DL123456"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(driverService, times(1)).isLicenseValid("DL123456");
    }
} 