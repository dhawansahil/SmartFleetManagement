#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

echo "Starting Smart Fleet Management System Integration Tests"
echo "====================================================="

# Function to test service health
test_service_health() {
    local service_name=$1
    local port=$2
    echo -e "\nTesting ${service_name} health..."
    
    response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:${port}/actuator/health)
    
    if [ "$response" == "200" ]; then
        echo -e "${GREEN}✓ ${service_name} is healthy${NC}"
        return 0
    else
        echo -e "${RED}✗ ${service_name} is not healthy${NC}"
        return 1
    fi
}

# Function to test API endpoint
test_api_endpoint() {
    local service_name=$1
    local endpoint=$2
    local method=$3
    local data=$4
    echo -e "\nTesting ${service_name} ${endpoint}..."
    
    if [ "$method" == "GET" ]; then
        response=$(curl -s -X GET http://localhost:${endpoint})
    elif [ "$method" == "POST" ]; then
        response=$(curl -s -X POST -H "Content-Type: application/json" -d "${data}" http://localhost:${endpoint})
    fi
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ ${service_name} ${endpoint} test passed${NC}"
        echo "Response: ${response}"
        return 0
    else
        echo -e "${RED}✗ ${service_name} ${endpoint} test failed${NC}"
        return 1
    fi
}

# Test Driver Service
echo -e "\nTesting Driver Service..."
test_service_health "Driver Service" 8081

# Test creating a driver
driver_data='{
    "firstName": "John",
    "lastName": "Doe",
    "licenseNumber": "LIC123",
    "phoneNumber": "+1234567890",
    "email": "john.doe@example.com",
    "status": "ACTIVE"
}'
test_api_endpoint "Driver Service" "8081/api/drivers" "POST" "${driver_data}"

# Test Vehicle Service
echo -e "\nTesting Vehicle Service..."
test_service_health "Vehicle Service" 8082

# Test creating a vehicle
vehicle_data='{
    "make": "Toyota",
    "model": "Camry",
    "year": 2023,
    "licensePlate": "ABC123",
    "status": "AVAILABLE"
}'
test_api_endpoint "Vehicle Service" "8082/api/vehicles" "POST" "${vehicle_data}"

# Test Maintenance Service
echo -e "\nTesting Maintenance Service..."
test_service_health "Maintenance Service" 8083

# Test creating a maintenance record
maintenance_data='{
    "vehicleId": 1,
    "type": "ROUTINE",
    "scheduledDate": "2024-03-20",
    "status": "SCHEDULED"
}'
test_api_endpoint "Maintenance Service" "8083/api/maintenance" "POST" "${maintenance_data}"

# Test AI Agent Service
echo -e "\nTesting AI Agent Service..."
test_service_health "AI Agent Service" 8084

# Test n8n workflow
echo -e "\nTesting n8n workflow..."
test_service_health "n8n" 5678

echo -e "\nIntegration Tests Completed"
echo "==========================" 