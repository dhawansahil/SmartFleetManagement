package com.fleet.aiagent.service.impl;

import com.fleet.aiagent.service.AIAgentService;
import com.fleet.aiagent.model.PredictionResult;
import com.fleet.aiagent.model.Recommendation;
import com.fleet.aiagent.model.AnomalyDetection;
import com.fleet.aiagent.client.VehicleServiceClient;
import com.fleet.aiagent.client.MaintenanceServiceClient;
import com.fleet.aiagent.client.DriverServiceClient;
import com.fleet.aiagent.config.AIConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIAgentServiceImpl implements AIAgentService {

    private final VehicleServiceClient vehicleServiceClient;
    private final MaintenanceServiceClient maintenanceServiceClient;
    private final DriverServiceClient driverServiceClient;
    private final RestTemplate restTemplate;
    private final AIConfig aiConfig;

    @Override
    public PredictionResult predictMaintenanceNeeds(Long vehicleId, int daysAhead) {
        log.info("Predicting maintenance needs for vehicle {} for next {} days", vehicleId, daysAhead);
        
        // 1. Gather vehicle data
        var vehicle = vehicleServiceClient.getVehicleById(vehicleId);
        var maintenanceHistory = maintenanceServiceClient.getMaintenanceHistory(vehicleId);
        
        // 2. Prepare features for AI model
        Map<String, Object> features = prepareFeatures(vehicle, maintenanceHistory);
        
        // 3. Call AI model through n8n webhook
        var prediction = callAIModel("predict-maintenance", features);
        
        // 4. Process and return prediction
        return processPrediction(prediction, vehicleId);
    }

    @Override
    public List<PredictionResult> predictFleetMaintenanceNeeds(int daysAhead) {
        log.info("Predicting maintenance needs for entire fleet for next {} days", daysAhead);
        
        // 1. Get all active vehicles
        var vehicles = vehicleServiceClient.getAllActiveVehicles();
        
        // 2. Predict maintenance for each vehicle
        return vehicles.stream()
                .map(vehicle -> predictMaintenanceNeeds(vehicle.getId(), daysAhead))
                .filter(prediction -> prediction.getConfidence() > 0.7)
                .collect(Collectors.toList());
    }

    @Override
    public Recommendation optimizeMaintenanceSchedule(Long vehicleId) {
        log.info("Optimizing maintenance schedule for vehicle {}", vehicleId);
        
        // 1. Get current maintenance schedule
        var maintenanceSchedule = maintenanceServiceClient.getMaintenanceSchedule(vehicleId);
        
        // 2. Get vehicle usage patterns
        var usagePatterns = vehicleServiceClient.getVehicleUsagePatterns(vehicleId);
        
        // 3. Prepare optimization request
        Map<String, Object> optimizationRequest = new HashMap<>();
        optimizationRequest.put("maintenanceSchedule", maintenanceSchedule);
        optimizationRequest.put("usagePatterns", usagePatterns);
        
        // 4. Call AI model for optimization
        var optimization = callAIModel("optimize-maintenance", optimizationRequest);
        
        // 5. Process and return recommendation
        return processOptimization(optimization);
    }

    @Override
    public List<AnomalyDetection> detectMaintenanceAnomalies(Long vehicleId) {
        log.info("Detecting maintenance anomalies for vehicle {}", vehicleId);
        
        // 1. Get recent maintenance records
        var maintenanceRecords = maintenanceServiceClient.getRecentMaintenanceRecords(vehicleId);
        
        // 2. Get vehicle metrics
        var vehicleMetrics = vehicleServiceClient.getVehicleMetrics(vehicleId);
        
        // 3. Prepare anomaly detection request
        Map<String, Object> detectionRequest = new HashMap<>();
        detectionRequest.put("maintenanceRecords", maintenanceRecords);
        detectionRequest.put("vehicleMetrics", vehicleMetrics);
        
        // 4. Call AI model for anomaly detection
        var anomalies = callAIModel("detect-anomalies", detectionRequest);
        
        // 5. Process and return anomalies
        return processAnomalies(anomalies);
    }

    @Override
    public double calculateVehicleRiskScore(Long vehicleId) {
        log.info("Calculating risk score for vehicle {}", vehicleId);
        
        // 1. Get vehicle data
        var vehicle = vehicleServiceClient.getVehicleById(vehicleId);
        var maintenanceHistory = maintenanceServiceClient.getMaintenanceHistory(vehicleId);
        var anomalies = detectMaintenanceAnomalies(vehicleId);
        
        // 2. Prepare risk assessment request
        Map<String, Object> riskRequest = new HashMap<>();
        riskRequest.put("vehicle", vehicle);
        riskRequest.put("maintenanceHistory", maintenanceHistory);
        riskRequest.put("anomalies", anomalies);
        
        // 3. Call AI model for risk assessment
        var riskAssessment = callAIModel("assess-risk", riskRequest);
        
        // 4. Calculate and return risk score
        return calculateRiskScore(riskAssessment);
    }

    private Map<String, Object> prepareFeatures(Object vehicle, List<?> maintenanceHistory) {
        Map<String, Object> features = new HashMap<>();
        // Add vehicle features
        features.put("mileage", ((Vehicle) vehicle).getMileage());
        features.put("age", LocalDate.now().getYear() - ((Vehicle) vehicle).getYear());
        features.put("lastServiceDate", ((Vehicle) vehicle).getLastServiceDate());
        
        // Add maintenance history features
        features.put("maintenanceFrequency", maintenanceHistory.size());
        features.put("averageCost", calculateAverageCost(maintenanceHistory));
        features.put("lastMaintenanceType", getLastMaintenanceType(maintenanceHistory));
        
        return features;
    }

    private Object callAIModel(String endpoint, Map<String, Object> data) {
        String n8nWebhookUrl = aiConfig.getN8nWebhookUrl() + "/" + endpoint;
        return restTemplate.postForObject(n8nWebhookUrl, data, Object.class);
    }

    private PredictionResult processPrediction(Object prediction, Long vehicleId) {
        // Process AI model response and create PredictionResult
        PredictionResult result = new PredictionResult();
        result.setVehicleId(vehicleId);
        // Set other fields based on AI model response
        return result;
    }

    private Recommendation processOptimization(Object optimization) {
        // Process AI model response and create Recommendation
        Recommendation recommendation = new Recommendation();
        // Set fields based on AI model response
        return recommendation;
    }

    private List<AnomalyDetection> processAnomalies(Object anomalies) {
        // Process AI model response and create list of AnomalyDetection
        List<AnomalyDetection> result = new ArrayList<>();
        // Add anomalies based on AI model response
        return result;
    }

    private double calculateRiskScore(Object riskAssessment) {
        // Calculate risk score based on AI model response
        return 0.0; // Placeholder
    }

    private double calculateAverageCost(List<?> maintenanceHistory) {
        return maintenanceHistory.stream()
                .mapToDouble(record -> ((MaintenanceRecord) record).getCost())
                .average()
                .orElse(0.0);
    }

    private String getLastMaintenanceType(List<?> maintenanceHistory) {
        if (maintenanceHistory.isEmpty()) {
            return "NONE";
        }
        return ((MaintenanceRecord) maintenanceHistory.get(0)).getMaintenanceType().toString();
    }
} 