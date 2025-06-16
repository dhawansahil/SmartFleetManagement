package com.fleet.vehicle.controller;

import com.fleet.vehicle.model.Vehicle;
import com.fleet.vehicle.model.VehicleStatus;
import com.fleet.vehicle.model.VehicleType;
import com.fleet.vehicle.service.VehicleService;
import com.fleet.vehicle.exception.VehicleNotFoundException;
import com.fleet.vehicle.exception.DuplicateVehicleException;
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

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void createVehicle_Success() throws Exception {
        when(vehicleService.createVehicle(any(Vehicle.class))).thenReturn(testVehicle);

        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testVehicle)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.registrationNumber").value("ABC123"))
                .andExpect(jsonPath("$.make").value("Toyota"));

        verify(vehicleService, times(1)).createVehicle(any(Vehicle.class));
    }

    @Test
    void createVehicle_DuplicateRegistration() throws Exception {
        when(vehicleService.createVehicle(any(Vehicle.class)))
                .thenThrow(new DuplicateVehicleException("Vehicle with registration number already exists"));

        mockMvc.perform(post("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testVehicle)))
                .andExpect(status().isBadRequest());

        verify(vehicleService, times(1)).createVehicle(any(Vehicle.class));
    }

    @Test
    void getVehicleById_Success() throws Exception {
        when(vehicleService.getVehicleById(anyLong())).thenReturn(Optional.of(testVehicle));

        mockMvc.perform(get("/api/v1/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.registrationNumber").value("ABC123"));

        verify(vehicleService, times(1)).getVehicleById(1L);
    }

    @Test
    void getVehicleById_NotFound() throws Exception {
        when(vehicleService.getVehicleById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/vehicles/1"))
                .andExpect(status().isNotFound());

        verify(vehicleService, times(1)).getVehicleById(1L);
    }

    @Test
    void getAllVehicles_Success() throws Exception {
        when(vehicleService.getAllVehicles()).thenReturn(testVehicles);

        mockMvc.perform(get("/api/v1/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].registrationNumber").value("ABC123"));

        verify(vehicleService, times(1)).getAllVehicles();
    }

    @Test
    void updateVehicle_Success() throws Exception {
        when(vehicleService.updateVehicle(anyLong(), any(Vehicle.class))).thenReturn(testVehicle);

        mockMvc.perform(put("/api/v1/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testVehicle)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.registrationNumber").value("ABC123"));

        verify(vehicleService, times(1)).updateVehicle(eq(1L), any(Vehicle.class));
    }

    @Test
    void updateVehicle_NotFound() throws Exception {
        when(vehicleService.updateVehicle(anyLong(), any(Vehicle.class)))
                .thenThrow(new VehicleNotFoundException("Vehicle not found"));

        mockMvc.perform(put("/api/v1/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testVehicle)))
                .andExpect(status().isNotFound());

        verify(vehicleService, times(1)).updateVehicle(eq(1L), any(Vehicle.class));
    }

    @Test
    void deleteVehicle_Success() throws Exception {
        doNothing().when(vehicleService).deleteVehicle(anyLong());

        mockMvc.perform(delete("/api/v1/vehicles/1"))
                .andExpect(status().isNoContent());

        verify(vehicleService, times(1)).deleteVehicle(1L);
    }

    @Test
    void deleteVehicle_NotFound() throws Exception {
        doThrow(new VehicleNotFoundException("Vehicle not found"))
                .when(vehicleService).deleteVehicle(anyLong());

        mockMvc.perform(delete("/api/v1/vehicles/1"))
                .andExpect(status().isNotFound());

        verify(vehicleService, times(1)).deleteVehicle(1L);
    }

    @Test
    void getVehiclesByType_Success() throws Exception {
        when(vehicleService.getVehiclesByType(any(VehicleType.class))).thenReturn(testVehicles);

        mockMvc.perform(get("/api/v1/vehicles/type/SEDAN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vehicleType").value("SEDAN"));

        verify(vehicleService, times(1)).getVehiclesByType(VehicleType.SEDAN);
    }

    @Test
    void getVehiclesByStatus_Success() throws Exception {
        when(vehicleService.getVehiclesByStatus(any(VehicleStatus.class))).thenReturn(testVehicles);

        mockMvc.perform(get("/api/v1/vehicles/status/ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));

        verify(vehicleService, times(1)).getVehiclesByStatus(VehicleStatus.ACTIVE);
    }

    @Test
    void getVehiclesNeedingService_Success() throws Exception {
        when(vehicleService.getVehiclesNeedingService(anyInt())).thenReturn(testVehicles);

        mockMvc.perform(get("/api/v1/vehicles/needing-service")
                .param("daysThreshold", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(vehicleService, times(1)).getVehiclesNeedingService(30);
    }

    @Test
    void getVehicleByRegistrationNumber_Success() throws Exception {
        when(vehicleService.getVehicleByRegistrationNumber(anyString())).thenReturn(Optional.of(testVehicle));

        mockMvc.perform(get("/api/v1/vehicles/registration/ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber").value("ABC123"));

        verify(vehicleService, times(1)).getVehicleByRegistrationNumber("ABC123");
    }
} 