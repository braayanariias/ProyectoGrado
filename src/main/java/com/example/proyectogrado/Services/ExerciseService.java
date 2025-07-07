package com.example.proyectogrado.Services;

import com.example.proyectogrado.Models.Exercise;
import com.example.proyectogrado.Models.Student;
import com.example.proyectogrado.Repositorys.ExerciseRepository;
import com.example.proyectogrado.Repositorys.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExerciseService {
    
    private final ExerciseRepository exerciseRepository;
    private final StudentRepository studentRepository;
    
    public ExerciseService(ExerciseRepository exerciseRepository, StudentRepository studentRepository) {
        this.exerciseRepository = exerciseRepository;
        this.studentRepository = studentRepository;
    }
    
    public Exercise saveExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }
    
    public Exercise createAndSaveExercise(String exerciseContent, Student student) {
        // Obtener la instancia gestionada del Student desde la base de datos
        Student managedStudent = studentRepository.findById(student.getId())
                .orElseThrow(() -> new RuntimeException("Student not found: " + student.getId()));
        
        Exercise exercise = new Exercise();
        exercise.setExerciseContent(exerciseContent);
        exercise.setStudent(managedStudent);
        exercise.setIsCompleted(false);
        
        return exerciseRepository.save(exercise);
    }
    
    public List<Exercise> getExercisesByStudent(Student student) {
        return exerciseRepository.findByStudent(student);
    }
    
    public List<Exercise> getExercisesByStudentId(UUID studentId) {
        return exerciseRepository.findByStudentIdOrderByAssignedDateDesc(studentId);
    }
    
    public List<Exercise> getPendingExercises() {
        return exerciseRepository.findByIsCompletedFalse();
    }
    
    public Exercise markAsCompleted(UUID exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));
        exercise.setIsCompleted(true);
        return exerciseRepository.save(exercise);
    }
    
    public Optional<Exercise> getExerciseById(UUID exerciseId) {
        return exerciseRepository.findById(exerciseId);
    }
}
