package com.example.proyectogrado.Models.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
}
