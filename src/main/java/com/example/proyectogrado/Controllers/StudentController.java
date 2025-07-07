package com.example.proyectogrado.Controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.proyectogrado.Models.Student;
import com.example.proyectogrado.Models.DTOs.StudentDTO;
import com.example.proyectogrado.Services.StudentService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/save")
    public Student saveStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }
    
    @PostMapping("/create")
    public Student createStudent(@RequestBody StudentDTO studentDTO) {
        Student student = new Student();
        student.setId(studentDTO.getId()); // UUID de Supabase
        student.setFullName(studentDTO.getFullName());
        student.setEmail(studentDTO.getEmail());
        return studentService.saveStudent(student);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable UUID id, @RequestBody StudentDTO studentDTO) {
        Student student = new Student();
        student.setId(id);
        student.setFullName(studentDTO.getFullName());
        student.setEmail(studentDTO.getEmail());
        Student updatedStudent = studentService.saveStudent(student);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @PostMapping("/sync-from-supabase")
    public ResponseEntity<Student> syncStudentFromSupabase(@RequestBody StudentDTO studentDTO) {
        // Validar que el DTO tenga todos los campos requeridos
        if (studentDTO.getId() == null || studentDTO.getEmail() == null || studentDTO.getFullName() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Student student = new Student();
        student.setId(studentDTO.getId()); // UUID generado por Supabase
        student.setFullName(studentDTO.getFullName());
        student.setEmail(studentDTO.getEmail());
        
        Student savedStudent = studentService.saveStudent(student);
        return ResponseEntity.ok(savedStudent);
    }
    
}
