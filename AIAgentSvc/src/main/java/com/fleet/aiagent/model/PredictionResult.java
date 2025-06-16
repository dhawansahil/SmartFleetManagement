package com.fleet.aiagent.model;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PredictionResult {
    private Long vehicleId;
    private String maintenanceType;
    private LocalDate predictedDate;
    private double confidence;
    private List<String> recommendedActions;
    private String explanation;
    private double estimatedCost;
    private int priority;
    private String riskLevel;
} 