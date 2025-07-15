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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/student")
@Tag(name = "Student", description = "API para gestión de estudiantes")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/save")
    @Operation(summary = "Guardar estudiante", description = "Guarda un estudiante en la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estudiante guardado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del estudiante inválidos")
    })
    public Student saveStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }
    
    @PostMapping("/create")
    @Operation(summary = "Crear estudiante", description = "Crea un nuevo estudiante usando un DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estudiante creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del DTO inválidos")
    })
    public Student createStudent(@RequestBody StudentDTO studentDTO) {
        Student student = new Student();
        student.setId(studentDTO.getId()); // UUID de Supabase
        student.setFullName(studentDTO.getFullName());
        student.setEmail(studentDTO.getEmail());
        return studentService.saveStudent(student);
    }
    
    @PutMapping("/update/{id}")
    @Operation(summary = "Actualizar estudiante", description = "Actualiza los datos de un estudiante por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estudiante actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Student> updateStudent(
            @Parameter(description = "ID del estudiante a actualizar") @PathVariable UUID id, 
            @RequestBody StudentDTO studentDTO) {
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
