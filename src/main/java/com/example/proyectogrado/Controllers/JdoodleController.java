package com.example.proyectogrado.Controllers;

import com.example.proyectogrado.Services.JdoodleService;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.proyectogrado.Models.JdoodleRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/jdoodle")
public class JdoodleController {

    private final JdoodleService jdoodleService;

    public JdoodleController(JdoodleService jdoodleService) {
        this.jdoodleService = jdoodleService;
    }

    @PostMapping("/execute")
    public Mono<String> executeCode(@RequestBody JdoodleRequest request) {
        return jdoodleService.executeCode(
                request.getScript(),
                request.getStdin() != null ? request.getStdin() : "",
                request.getLanguage(),
                request.getVersionIndex(),
                request.isCompileOnly()
        );
    }
}
