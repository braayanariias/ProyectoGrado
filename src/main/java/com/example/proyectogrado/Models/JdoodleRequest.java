package com.example.proyectogrado.Models;

import lombok.Data;

@Data
public class JdoodleRequest {
    private String script;
    private String stdin;
    private String language;
    private String versionIndex;
    private boolean compileOnly;
}
