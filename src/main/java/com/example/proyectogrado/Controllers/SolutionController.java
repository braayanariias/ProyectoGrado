package com.example.proyectogrado.Controllers;

import com.example.proyectogrado.Models.DTOs.SolutionSubmissionDTO;
import com.example.proyectogrado.Models.DTOs.SolutionResponseDTO;
import com.example.proyectogrado.Exceptions.CodeCompilationException;
import com.example.proyectogrado.Models.Solution;
import com.example.proyectogrado.Services.SolutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/solutions")
@CrossOrigin(origins = "*")
@Tag(name = "Solution", description = "API para gestión de soluciones de ejercicios")
public class SolutionController {

    private final SolutionService solutionService;

    public SolutionController(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    @PostMapping("/submit")
    @Operation(summary = "Enviar solución", 
              description = "Envía una solución de código para ser evaluada y compilada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solución procesada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de compilación o datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> submitSolution(@RequestBody SolutionSubmissionDTO submissionDTO) {
        try {
            SolutionResponseDTO response = solutionService.submitSolution(submissionDTO);
            return ResponseEntity.ok(response);
        } catch (CodeCompilationException e) {
            // Error de compilación específico - será manejado por GlobalExceptionHandler
            throw e;
        } catch (RuntimeException e) {
            // Otros errores de runtime
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "SUBMISSION_ERROR");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/student/email/{email}")
    @Operation(summary = "Obtener soluciones por email", 
              description = "Retorna todas las soluciones enviadas por un estudiante usando su email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de soluciones encontrada"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    public ResponseEntity<List<SolutionResponseDTO>> getSolutionsByStudentEmail(
            @Parameter(description = "Email del estudiante") @PathVariable String email) {
        List<SolutionResponseDTO> solutions = solutionService.getSolutionsByStudentEmail(email);
        return ResponseEntity.ok(solutions);
    }

    @GetMapping("/exercise/{exerciseId}")
    public ResponseEntity<List<SolutionResponseDTO>> getSolutionsByExerciseId(@PathVariable UUID exerciseId) {
        List<SolutionResponseDTO> solutions = solutionService.getSolutionsByExerciseId(exerciseId);
        return ResponseEntity.ok(solutions);
    }

    @GetMapping("/{solutionId}")
    public ResponseEntity<SolutionResponseDTO> getSolutionById(@PathVariable UUID solutionId) {
        Optional<SolutionResponseDTO> solution = solutionService.getSolutionById(solutionId);
        if (solution.isPresent()) {
            return ResponseEntity.ok(solution.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solutions/{exerciseId}/{studentId}")
    public List<Solution> getSolutionsByExerciseAndStudent(
            @PathVariable UUID exerciseId, @PathVariable UUID studentId) {
        return solutionService.getSolutionsByExerciseIdAndStudentId(exerciseId, studentId);
    }
}
