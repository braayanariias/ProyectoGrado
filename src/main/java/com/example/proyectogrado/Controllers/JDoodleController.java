package com.example.proyectogrado.Controllers;

import com.example.proyectogrado.Models.DTOs.JDoodleResponseDTO;
import com.example.proyectogrado.Services.JDoodleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/jdoodle")
@CrossOrigin(origins = "*")
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

    /**
     * Endpoint para probar la validación de código Java con JDoodle
     * @param request Map con el código Java a validar
     * @return JDoodleResponseDTO con el resultado de la compilación
     */
    @PostMapping("/validate")
    public ResponseEntity<JDoodleResponseDTO> validateCode(@RequestBody Map<String, String> request) {
        try {
            String code = request.get("code");
            if (code == null || code.trim().isEmpty()) {
                JDoodleResponseDTO errorResponse = new JDoodleResponseDTO();
                errorResponse.setCompiled(false);
                errorResponse.setError("El código no puede estar vacío");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            JDoodleResponseDTO response = jDoodleService.validateJavaCode(code);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            JDoodleResponseDTO errorResponse = new JDoodleResponseDTO();
            errorResponse.setCompiled(false);
            errorResponse.setError("Error interno: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Endpoint de prueba para verificar la conectividad con JDoodle
     * @return Mensaje de estado
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testConnection() {
        try {
            // Código Java simple para probar
            String testCode = "public class Test { public static void main(String[] args) { System.out.println(\"Hello, World!\"); } }";
            
            JDoodleResponseDTO response = jDoodleService.validateJavaCode(testCode);
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", response.isCompiled() ? "success" : "error");
            result.put("message", response.isCompiled() ? "Conexión con JDoodle exitosa" : "Error en la conexión con JDoodle");
            result.put("isCompiled", response.isCompiled());
            result.put("output", response.getOutput());
            result.put("statusCode", response.getStatusCode());
            result.put("cpuTime", response.getCpuTime());
            result.put("memory", response.getMemory());
            result.put("compilationStatus", response.getCompilationStatus());
            result.put("projectKey", response.getProjectKey());
            if (response.getError() != null) {
                result.put("error", response.getError());
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "Error interno");
            errorResult.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResult);
        }
    }
}
