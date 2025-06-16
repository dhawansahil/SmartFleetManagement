# Smart Fleet Management System - Integration Tests

## Test Plan

### 1. Individual Service Tests

#### Driver Service Tests
- [ ] Basic CRUD operations
- [ ] Query endpoint for n8n
- [ ] Validation rules
- [ ] Error handling

#### Vehicle Service Tests
- [ ] Basic CRUD operations
- [ ] Vehicle status management
- [ ] Maintenance scheduling
- [ ] Error handling

#### Maintenance Service Tests
- [ ] Basic CRUD operations
- [ ] Maintenance scheduling
- [ ] Status updates
- [ ] Error handling

#### AI Agent Service Tests
- [ ] Workflow integration
- [ ] Data processing
- [ ] Decision making
- [ ] Error handling

### 2. Integration Tests

#### Service-to-Service Communication
- [ ] Driver-Vehicle integration
- [ ] Vehicle-Maintenance integration
- [ ] AI Agent with all services

#### n8n Workflow Tests
- [ ] Driver management workflow
- [ ] Maintenance scheduling workflow
- [ ] Vehicle status workflow
- [ ] Error handling and recovery

### 3. End-to-End Tests

#### Business Scenarios
- [ ] New driver onboarding
- [ ] Vehicle assignment
- [ ] Maintenance scheduling
- [ ] Status updates
- [ ] Report generation

## Test Data

### Sample Driver Data
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "licenseNumber": "LIC123",
  "phoneNumber": "+1234567890",
  "email": "john.doe@example.com",
  "status": "ACTIVE"
}
```

### Sample Vehicle Data
```json
{
  "make": "Toyota",
  "model": "Camry",
  "year": 2023,
  "licensePlate": "ABC123",
  "status": "AVAILABLE"
}
```

### Sample Maintenance Data
```json
{
  "vehicleId": 1,
  "type": "ROUTINE",
  "scheduledDate": "2024-03-20",
  "status": "SCHEDULED"
}
```

## Running Tests

1. Start all services:
```bash
# Start Driver Service
cd DriverSvc
mvn spring-boot:run

# Start Vehicle Service
cd VehicleSvc
mvn spring-boot:run

# Start Maintenance Service
cd MaintenanceSvc
mvn spring-boot:run

# Start AI Agent Service
cd AIAgentSvc
mvn spring-boot:run
```

2. Start n8n:
```bash
n8n start
```

3. Run integration tests:
```bash
cd IntegrationTests
mvn test
``` 