package com.fleet.aiagent.model;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class Recommendation {
    private String type;
    private String description;
    private double expectedBenefit;
    private List<String> actions;
    private Map<String, Object> parameters;
    private LocalDate implementationDate;
    private int priority;
    private String status;
    private Map<String, Double> metrics;
} 