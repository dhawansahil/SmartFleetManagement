package com.fleet.maintenance.exception;
 
public class MaintenanceRecordNotFoundException extends RuntimeException {
    public MaintenanceRecordNotFoundException(String message) {
        super(message);
    }
} 