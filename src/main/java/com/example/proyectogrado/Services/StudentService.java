package com.example.proyectogrado.Services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.proyectogrado.Models.Student;
import com.example.proyectogrado.Repositorys.StudentRepository;

@Service
@Transactional
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {
        // Si el student tiene un ID, es una actualización
        if (student.getId() != null) {
            return updateStudent(student);
        }
        
        // Si no tiene ID, verificar si ya existe por email
        Optional<Student> existingStudent = studentRepository.findByEmail(student.getEmail());
        if (existingStudent.isPresent()) {
            // Si ya existe, actualizar la información
            Student existing = existingStudent.get();
            existing.setFullName(student.getFullName());
            return studentRepository.save(existing);
        } else {
            // Si no existe, crear uno nuevo
            return studentRepository.save(student);
        }
    }
    
    private Student updateStudent(Student student) {
        // Buscar el estudiante existente por ID
        Optional<Student> existingStudentOpt = studentRepository.findById(student.getId());
        
        if (existingStudentOpt.isPresent()) {
            Student existing = existingStudentOpt.get();
            // Actualizar solo los campos necesarios
            existing.setFullName(student.getFullName());
            existing.setEmail(student.getEmail());
            return studentRepository.save(existing);
        } else {
            // Si no existe con ese ID, verificar por email
            Optional<Student> existingByEmail = studentRepository.findByEmail(student.getEmail());
            if (existingByEmail.isPresent()) {
                Student existing = existingByEmail.get();
                existing.setFullName(student.getFullName());
                return studentRepository.save(existing);
            } else {
                // Si no existe ni por ID ni por email, crear uno nuevo pero sin el ID
                Student newStudent = new Student();
                newStudent.setFullName(student.getFullName());
                newStudent.setEmail(student.getEmail());
                return studentRepository.save(newStudent);
            }
        }
    }
    
}
