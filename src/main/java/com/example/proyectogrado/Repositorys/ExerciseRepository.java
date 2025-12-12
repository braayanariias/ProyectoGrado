package com.example.proyectogrado.Repositorys;

import com.example.proyectogrado.Models.Exercise;
import com.example.proyectogrado.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    
    List<Exercise> findByStudent(Student student);
    
    List<Exercise> findByStudentIdOrderByAssignedDateDesc(UUID studentId);
    
    List<Exercise> findByIsCompletedFalse();

    //Obtener ejercicios pendientes por estudiante
    List<Exercise> findByIsCompletedFalseAndStudent(Student student);

}
