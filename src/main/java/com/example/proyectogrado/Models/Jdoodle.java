package com.example.proyectogrado.Models;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Jdoodle {

    @Value("${jdoodle.client.id}")
    private String clientId;
    @Value("${jdoodle.client.secret}")
    private String clientSecret;
    private String script;
    private String stdin;
    private String language;
    private String versionIndex;
    private String compileOnly;

}
