package com.example.proyectogrado.Controllers;

import com.example.proyectogrado.Models.Exercise;
import com.example.proyectogrado.Models.Student;
import com.example.proyectogrado.Services.ExerciseService;
import com.example.proyectogrado.Services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = "*")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final StudentService studentService;

    public ExerciseController(ExerciseService exerciseService, StudentService studentService) {
        this.exerciseService = exerciseService;
        this.studentService = studentService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Exercise>> getExercisesByStudentId(@PathVariable UUID studentId) {
        List<Exercise> exercises = exerciseService.getExercisesByStudentId(studentId);
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/student/email/{email}")
    public ResponseEntity<List<Exercise>> getExercisesByStudentEmail(@PathVariable String email) {
        Student student = studentService.findByEmail(email);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        List<Exercise> exercises = exerciseService.getExercisesByStudent(student);
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Exercise>> getPendingExercises() {
        List<Exercise> exercises = exerciseService.getPendingExercises();
        return ResponseEntity.ok(exercises);
    }

    @PutMapping("/{exerciseId}/complete")
    public ResponseEntity<Exercise> markExerciseAsCompleted(@PathVariable UUID exerciseId) {
        try {
            Exercise completedExercise = exerciseService.markAsCompleted(exerciseId);
            return ResponseEntity.ok(completedExercise);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{exerciseId}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable UUID exerciseId) {
        Optional<Exercise> exercise = exerciseService.getExerciseById(exerciseId);
        if (exercise.isPresent()) {
            return ResponseEntity.ok(exercise.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
