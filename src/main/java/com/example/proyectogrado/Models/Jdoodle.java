package com.example.proyectogrado.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Jdoodle {

    private String clientId;
    private String clientSecret;
    private String script;
    private String stdin;
    private String language;
    private String versionIndex;
    private String compileOnly;

}
