package com.example.proyectogrado.Models.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JDoodleResponseDTO {
    
    @JsonProperty("output")
    private String output;
    
    @JsonProperty("statusCode")
    private int statusCode;
    
    @JsonProperty("memory")
    private String memory;
    
    @JsonProperty("cpuTime")
    private String cpuTime;
    
    @JsonProperty("compilationStatus")
    private String compilationStatus;
    
    @JsonProperty("isCompiled")
    private boolean isCompiled;
    
    @JsonProperty("error")
    private String error;
    
    @JsonProperty("projectKey")
    private String projectKey;

    // Constructores
    public JDoodleResponseDTO() {}

    // Getters y Setters
    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(String cpuTime) {
        this.cpuTime = cpuTime;
    }

    public String getCompilationStatus() {
        return compilationStatus;
    }

    public void setCompilationStatus(String compilationStatus) {
        this.compilationStatus = compilationStatus;
    }

    public boolean isCompiled() {
        return isCompiled;
    }

    public void setCompiled(boolean compiled) {
        isCompiled = compiled;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }
}
