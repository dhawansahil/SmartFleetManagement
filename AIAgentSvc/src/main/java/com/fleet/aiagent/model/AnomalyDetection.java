package com.fleet.aiagent.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

@Data
public class AnomalyDetection {
    private String type;
    private String description;
    private double severity;
    private LocalDateTime detectedAt;
    private Map<String, Object> metrics;
    private String source;
    private String status;
    private List<String> recommendedActions;
    private double confidence;
} 