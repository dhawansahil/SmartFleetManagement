package com.fleet.integration;

import com.fleet.driver.model.Driver;
import com.fleet.driver.model.DriverStatus;
import com.fleet.maintenance.model.MaintenanceRecord;
import com.fleet.maintenance.model.MaintenanceStatus;
import com.fleet.maintenance.model.MaintenanceType;
import com.fleet.vehicle.model.Vehicle;
import com.fleet.vehicle.model.VehicleStatus;
import com.fleet.vehicle.model.VehicleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl(String service) {
        return "http://localhost:" + port + "/" + service;
    }

    @Test
    void testDriverVehicleAssignment() {
        // Create a driver
        Driver driver = new Driver();
        driver.setFirstName("John");
        driver.setLastName("Doe");
        driver.setLicenseNumber("DL123456");
        driver.setLicenseExpiryDate(LocalDate.now().plusYears(1));
        driver.setEmail("john.doe@example.com");
        driver.setPhoneNumber("1234567890");
        driver.setStatus(DriverStatus.ACTIVE);

        ResponseEntity<Driver> driverResponse = restTemplate.postForEntity(
                getBaseUrl("driver-service/api/v1/drivers"),
                driver,
                Driver.class
        );
        assertEquals(HttpStatus.CREATED, driverResponse.getStatusCode());
        Driver createdDriver = driverResponse.getBody();
        assertNotNull(createdDriver);

        // Create a vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber("ABC123");
        vehicle.setMake("Toyota");
        vehicle.setModel("Camry");
        vehicle.setYear(2022);
        vehicle.setVehicleType(VehicleType.SEDAN);
        vehicle.setStatus(VehicleStatus.ACTIVE);
        vehicle.setMileage(10000);
        vehicle.setLastServiceDate(LocalDate.now().minusMonths(3));
        vehicle.setNextServiceDate(LocalDate.now().plusMonths(3));

        ResponseEntity<Vehicle> vehicleResponse = restTemplate.postForEntity(
                getBaseUrl("vehicle-service/api/v1/vehicles"),
                vehicle,
                Vehicle.class
        );
        assertEquals(HttpStatus.CREATED, vehicleResponse.getStatusCode());
        Vehicle createdVehicle = vehicleResponse.getBody();
        assertNotNull(createdVehicle);

        // Create a maintenance record
        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
        maintenanceRecord.setVehicleId(createdVehicle.getId());
        maintenanceRecord.setMaintenanceType(MaintenanceType.ROUTINE);
        maintenanceRecord.setDescription("Regular service");
        maintenanceRecord.setServiceDate(LocalDate.now());
        maintenanceRecord.setNextServiceDate(LocalDate.now().plusMonths(3));
        maintenanceRecord.setCost(200.0);
        maintenanceRecord.setServiceProvider("Toyota Service Center");
        maintenanceRecord.setMileage(10000);
        maintenanceRecord.setStatus(MaintenanceStatus.COMPLETED);

        ResponseEntity<MaintenanceRecord> maintenanceResponse = restTemplate.postForEntity(
                getBaseUrl("maintenance-service/api/v1/maintenance"),
                maintenanceRecord,
                MaintenanceRecord.class
        );
        assertEquals(HttpStatus.CREATED, maintenanceResponse.getStatusCode());
        MaintenanceRecord createdMaintenance = maintenanceResponse.getBody();
        assertNotNull(createdMaintenance);

        // Test getting maintenance records for a vehicle
        ResponseEntity<List> maintenanceListResponse = restTemplate.getForEntity(
                getBaseUrl("maintenance-service/api/v1/maintenance/vehicle/" + createdVehicle.getId()),
                List.class
        );
        assertEquals(HttpStatus.OK, maintenanceListResponse.getStatusCode());
        List<MaintenanceRecord> maintenanceRecords = maintenanceListResponse.getBody();
        assertNotNull(maintenanceRecords);
        assertFalse(maintenanceRecords.isEmpty());

        // Test getting vehicles needing service
        ResponseEntity<List> vehiclesNeedingServiceResponse = restTemplate.getForEntity(
                getBaseUrl("vehicle-service/api/v1/vehicles/needing-service?daysThreshold=30"),
                List.class
        );
        assertEquals(HttpStatus.OK, vehiclesNeedingServiceResponse.getStatusCode());
        List<Vehicle> vehiclesNeedingService = vehiclesNeedingServiceResponse.getBody();
        assertNotNull(vehiclesNeedingService);

        // Test getting drivers with expiring licenses
        ResponseEntity<List> driversWithExpiringLicensesResponse = restTemplate.getForEntity(
                getBaseUrl("driver-service/api/v1/drivers/expiring-licenses?daysThreshold=30"),
                List.class
        );
        assertEquals(HttpStatus.OK, driversWithExpiringLicensesResponse.getStatusCode());
        List<Driver> driversWithExpiringLicenses = driversWithExpiringLicensesResponse.getBody();
        assertNotNull(driversWithExpiringLicenses);

        // Clean up
        restTemplate.delete(getBaseUrl("maintenance-service/api/v1/maintenance/" + createdMaintenance.getId()));
        restTemplate.delete(getBaseUrl("vehicle-service/api/v1/vehicles/" + createdVehicle.getId()));
        restTemplate.delete(getBaseUrl("driver-service/api/v1/drivers/" + createdDriver.getId()));
    }

    @Test
    void testMaintenanceCostTracking() {
        // Create a vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber("XYZ789");
        vehicle.setMake("Honda");
        vehicle.setModel("Civic");
        vehicle.setYear(2021);
        vehicle.setVehicleType(VehicleType.SEDAN);
        vehicle.setStatus(VehicleStatus.ACTIVE);
        vehicle.setMileage(15000);
        vehicle.setLastServiceDate(LocalDate.now().minusMonths(2));
        vehicle.setNextServiceDate(LocalDate.now().plusMonths(4));

        ResponseEntity<Vehicle> vehicleResponse = restTemplate.postForEntity(
                getBaseUrl("vehicle-service/api/v1/vehicles"),
                vehicle,
                Vehicle.class
        );
        assertEquals(HttpStatus.CREATED, vehicleResponse.getStatusCode());
        Vehicle createdVehicle = vehicleResponse.getBody();
        assertNotNull(createdVehicle);

        // Create multiple maintenance records
        MaintenanceRecord maintenance1 = new MaintenanceRecord();
        maintenance1.setVehicleId(createdVehicle.getId());
        maintenance1.setMaintenanceType(MaintenanceType.ROUTINE);
        maintenance1.setDescription("Oil change");
        maintenance1.setServiceDate(LocalDate.now().minusMonths(2));
        maintenance1.setNextServiceDate(LocalDate.now().plusMonths(4));
        maintenance1.setCost(100.0);
        maintenance1.setServiceProvider("Honda Service Center");
        maintenance1.setMileage(15000);
        maintenance1.setStatus(MaintenanceStatus.COMPLETED);

        MaintenanceRecord maintenance2 = new MaintenanceRecord();
        maintenance2.setVehicleId(createdVehicle.getId());
        maintenance2.setMaintenanceType(MaintenanceType.REPAIR);
        maintenance2.setDescription("Brake replacement");
        maintenance2.setServiceDate(LocalDate.now().minusMonths(1));
        maintenance2.setNextServiceDate(LocalDate.now().plusMonths(6));
        maintenance2.setCost(500.0);
        maintenance2.setServiceProvider("Honda Service Center");
        maintenance2.setMileage(16000);
        maintenance2.setStatus(MaintenanceStatus.COMPLETED);

        restTemplate.postForEntity(
                getBaseUrl("maintenance-service/api/v1/maintenance"),
                maintenance1,
                MaintenanceRecord.class
        );

        restTemplate.postForEntity(
                getBaseUrl("maintenance-service/api/v1/maintenance"),
                maintenance2,
                MaintenanceRecord.class
        );

        // Test getting total maintenance cost
        ResponseEntity<Double> totalCostResponse = restTemplate.getForEntity(
                getBaseUrl("maintenance-service/api/v1/maintenance/cost/vehicle/" + createdVehicle.getId()),
                Double.class
        );
        assertEquals(HttpStatus.OK, totalCostResponse.getStatusCode());
        Double totalCost = totalCostResponse.getBody();
        assertNotNull(totalCost);
        assertEquals(600.0, totalCost);

        // Test getting maintenance cost by date range
        ResponseEntity<Double> costByDateRangeResponse = restTemplate.getForEntity(
                getBaseUrl("maintenance-service/api/v1/maintenance/cost/date-range") +
                        "?startDate=" + LocalDate.now().minusMonths(3) +
                        "&endDate=" + LocalDate.now(),
                Double.class
        );
        assertEquals(HttpStatus.OK, costByDateRangeResponse.getStatusCode());
        Double costByDateRange = costByDateRangeResponse.getBody();
        assertNotNull(costByDateRange);
        assertEquals(600.0, costByDateRange);

        // Clean up
        restTemplate.delete(getBaseUrl("vehicle-service/api/v1/vehicles/" + createdVehicle.getId()));
    }
} 