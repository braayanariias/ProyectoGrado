package com.example.proyectogrado.Controllers;

import com.example.proyectogrado.Models.DTOs.SolutionSubmissionDTO;
import com.example.proyectogrado.Models.DTOs.SolutionResponseDTO;
import com.example.proyectogrado.Services.SolutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/solutions")
@CrossOrigin(origins = "*")
public class SolutionController {

    private final SolutionService solutionService;

    public SolutionController(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    @PostMapping("/submit")
    public ResponseEntity<SolutionResponseDTO> submitSolution(@RequestBody SolutionSubmissionDTO submissionDTO) {
        try {
            SolutionResponseDTO response = solutionService.submitSolution(submissionDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/student/email/{email}")
    public ResponseEntity<List<SolutionResponseDTO>> getSolutionsByStudentEmail(@PathVariable String email) {
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

    @GetMapping("/unevaluated")
    public ResponseEntity<List<SolutionResponseDTO>> getUnevaluatedSolutions() {
        List<SolutionResponseDTO> solutions = solutionService.getUnevaluatedSolutions();
        return ResponseEntity.ok(solutions);
    }

    @GetMapping("/latest/exercise/{exerciseId}/student/{email}")
    public ResponseEntity<SolutionResponseDTO> getLatestSolutionByExerciseAndStudent(
            @PathVariable UUID exerciseId, 
            @PathVariable String email) {
        Optional<SolutionResponseDTO> solution = solutionService.getLatestSolutionByExerciseAndStudent(exerciseId, email);
        if (solution.isPresent()) {
            return ResponseEntity.ok(solution.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
