package com.example.proyectogrado.Repositorys;

import com.example.proyectogrado.Models.Solution;
import com.example.proyectogrado.Models.Student;
import com.example.proyectogrado.Models.Exercise;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, UUID> {
    
    List<Solution> findByStudentId(UUID studentId);

    @Query("SELECT s FROM Solution s WHERE s.exercise.exerciseId = :exerciseId")
    List<Solution> findSolutionsByExerciseId(UUID exerciseId);
    
    List<Solution> findByStudentAndExercise(Student student, Exercise exercise);

    List<Solution> findSolutionsByExercise_ExerciseIdAndStudent_Id(UUID exerciseId, UUID studentId);
    
    @Query("SELECT s FROM Solution s WHERE s.student.email = :email")
    List<Solution> findByStudentEmail(@Param("email") String email);
    
    List<Solution> findByIsEvaluated(Boolean isEvaluated);
    
    @Query("SELECT s FROM Solution s WHERE s.exercise.exerciseId = :exerciseId AND s.student.id = :studentId ORDER BY s.submittedDate DESC")
    List<Solution> findByExerciseAndStudentOrderBySubmittedDateDesc(@Param("exerciseId") UUID exerciseId, @Param("studentId") UUID studentId);
    
    Optional<Solution> findFirstByExerciseAndStudentOrderBySubmittedDateDesc(Exercise exercise, Student student);
    
}
