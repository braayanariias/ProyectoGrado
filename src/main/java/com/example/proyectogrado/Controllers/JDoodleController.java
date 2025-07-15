package com.example.proyectogrado.Controllers;

import com.example.proyectogrado.Models.DTOs.JDoodleResponseDTO;
import com.example.proyectogrado.Services.JDoodleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/api/jdoodle")
@CrossOrigin(origins = "*")
@Tag(name = "JDoodle", description = "API para compilación y ejecución de código usando JDoodle")
public class JDoodleController {

    private final JDoodleService jDoodleService;

    public JDoodleController(JDoodleService jDoodleService) {
        this.jDoodleService = jDoodleService;
    }

    /**
     * Endpoint para probar solo la compilación de código Java con JDoodle (sin ejecutar)
     * @param request Map con el código Java a validar
     * @return JDoodleResponseDTO con el resultado de la compilación
     */
    @PostMapping("/compile-only")
    @Operation(summary = "Compilar código únicamente", 
              description = "Valida y compila código Java sin ejecutarlo usando JDoodle")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Código compilado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de compilación o código vacío"),
        @ApiResponse(responseCode = "500", description = "Error interno del servicio JDoodle")
    })
    public ResponseEntity<JDoodleResponseDTO> compileOnlyCode(@RequestBody Map<String, String> request) {
        try {
            String code = request.get("code");
            if (code == null || code.trim().isEmpty()) {
                JDoodleResponseDTO errorResponse = new JDoodleResponseDTO();
                errorResponse.setCompiled(false);
                errorResponse.setError("El código no puede estar vacío");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            JDoodleResponseDTO response = jDoodleService.validateJavaCodeCompileOnly(code);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            JDoodleResponseDTO errorResponse = new JDoodleResponseDTO();
            errorResponse.setCompiled(false);
            errorResponse.setError("Error interno: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
