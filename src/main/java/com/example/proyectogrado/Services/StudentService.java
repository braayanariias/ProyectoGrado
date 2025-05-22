package com.example.proyectogrado.Services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.proyectogrado.Models.Student;
import com.example.proyectogrado.Repositorys.StudentRepository;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(UUID id) {
        return studentRepository.findById(id).orElse(null);
    }
    
}
