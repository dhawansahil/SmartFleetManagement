package com.fleet.maintenance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "maintenance_records")
public class MaintenanceRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Vehicle ID is required")
    @Column(name = "vehicle_id")
    private Long vehicleId;
    
    @NotNull(message = "Maintenance type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "maintenance_type")
    private MaintenanceType maintenanceType;
    
    @NotBlank(message = "Description is required")
    @Column(name = "description")
    private String description;
    
    @NotNull(message = "Service date is required")
    @Column(name = "service_date")
    private LocalDateTime serviceDate;
    
    @NotNull(message = "Next service date is required")
    @Column(name = "next_service_date")
    private LocalDateTime nextServiceDate;
    
    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.0", message = "Cost must be greater than or equal to 0")
    @Column(name = "cost")
    private BigDecimal cost;
    
    @Column(name = "service_provider")
    private String serviceProvider;
    
    @Column(name = "mileage")
    private Integer mileage;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MaintenanceStatus status = MaintenanceStatus.COMPLETED;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 