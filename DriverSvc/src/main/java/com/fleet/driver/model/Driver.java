package com.fleet.driver.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "drivers")
public class Driver {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @NotBlank(message = "License number is required")
    @Column(name = "license_number", unique = true, nullable = false)
    private String licenseNumber;
    
    @NotNull(message = "License expiry date is required")
    @Column(name = "license_expiry_date")
    private LocalDate licenseExpiryDate;
    
    @Email(message = "Invalid email format")
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DriverStatus status = DriverStatus.ACTIVE;
    
    @Column(name = "created_at")
    private LocalDate createdAt;
    
    @Column(name = "updated_at")
    private LocalDate updatedAt;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "notes")
    private String notes;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
} 