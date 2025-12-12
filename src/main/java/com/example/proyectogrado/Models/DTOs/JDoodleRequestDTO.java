package com.example.proyectogrado.Models.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JDoodleRequestDTO {
    
    @JsonProperty("clientId")
    private String clientId;
    
    @JsonProperty("clientSecret")
    private String clientSecret;
    
    @JsonProperty("script")
    private String script;
    
    @JsonProperty("language")
    private String language;
    
    @JsonProperty("versionIndex")
    private String versionIndex;
    
    @JsonProperty("stdin")
    private String stdin;
    
    @JsonProperty("compileOnly")
    private boolean compileOnly;

    // Constructor personalizado para casos comunes
    public JDoodleRequestDTO(String clientId, String clientSecret, String script, String language, String versionIndex) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.script = script;
        this.language = language;
        this.versionIndex = versionIndex;
        this.stdin = "";
        this.compileOnly = false; // Por defecto ejecuta y compila
    }

    public JDoodleRequestDTO(String clientId, String clientSecret, String script, String language, String versionIndex, boolean compileOnly) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.script = script;
        this.language = language;
        this.versionIndex = versionIndex;
        this.stdin = "";
        this.compileOnly = compileOnly;
    }
}
