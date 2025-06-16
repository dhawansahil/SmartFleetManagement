package com.fleet.aiagent.service;

import com.fleet.aiagent.model.PredictionResult;
import com.fleet.aiagent.model.Recommendation;
import com.fleet.aiagent.model.AnomalyDetection;
import java.time.LocalDate;
import java.util.List;

public interface AIAgentService {
    
    // Predictive Maintenance
    PredictionResult predictMaintenanceNeeds(Long vehicleId, int daysAhead);
    List<PredictionResult> predictFleetMaintenanceNeeds(int daysAhead);
    
    // Cost Optimization
    Recommendation optimizeMaintenanceSchedule(Long vehicleId);
    Recommendation optimizeFleetRoutes();
    
    // Anomaly Detection
    List<AnomalyDetection> detectMaintenanceAnomalies(Long vehicleId);
    List<AnomalyDetection> detectDriverBehaviorAnomalies(Long driverId);
    
    // Resource Allocation
    Recommendation optimizeDriverAssignment();
    Recommendation optimizeVehicleAllocation();
    
    // Risk Assessment
    double calculateVehicleRiskScore(Long vehicleId);
    double calculateDriverRiskScore(Long driverId);
    
    // Performance Analytics
    Recommendation generateFleetPerformanceReport(LocalDate startDate, LocalDate endDate);
    Recommendation generateDriverPerformanceReport(Long driverId, LocalDate startDate, LocalDate endDate);
} 