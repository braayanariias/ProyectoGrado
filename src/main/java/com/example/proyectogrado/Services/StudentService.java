package com.example.proyectogrado.Services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.proyectogrado.Models.Student;
import com.example.proyectogrado.Models.DTOs.StudentDTO;
import com.example.proyectogrado.Repositorys.StudentRepository;

@Service
@Transactional
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {
        // Validar que el student tenga un ID (UUID de Supabase)
        if (student.getId() == null) {
            throw new IllegalArgumentException("El estudiante debe tener un UUID válido de Supabase");
        }
        
        // Verificar si ya existe por ID (UUID de Supabase)
        Optional<Student> existingById = studentRepository.findById(student.getId());
        if (existingById.isPresent()) {
            // Si existe por ID, actualizar la información
            Student existing = existingById.get();
            existing.setFullName(student.getFullName());
            existing.setEmail(student.getEmail());
            return studentRepository.save(existing);
        }
        
        // Verificar si existe por email (para casos de migración o inconsistencias)
        Optional<Student> existingByEmail = studentRepository.findByEmail(student.getEmail());
        if (existingByEmail.isPresent()) {
            // Si existe por email pero con diferente ID, actualizar el ID también
            Student existing = existingByEmail.get();
            existing.setId(student.getId()); // Actualizar con el UUID de Supabase
            existing.setFullName(student.getFullName());
            existing.setEmail(student.getEmail());
            return studentRepository.save(existing);
        }
        
        // Si no existe, crear uno nuevo con el UUID de Supabase
        return studentRepository.save(student);
    }
    
    // Método sobrecargado para recibir StudentDTO (recomendado para operaciones desde frontend)
    public Student saveStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setFullName(studentDTO.getFullName());
        student.setEmail(studentDTO.getEmail());
        // No seteamos exercises ya que será null (lazy loading las traerá cuando sea necesario)
        
        return saveStudent(student); // Reutiliza la lógica existente
    }
    
    public Student findByEmail(String email) {
        Optional<Student> student = studentRepository.findByEmail(email);
        return student.orElse(null);
    }
    
}
