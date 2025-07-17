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
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
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
    public ResponseEntity<Student> saveStudent(@RequestBody StudentDTO studentDTO) {
        try {
            Student savedStudent = studentService.saveStudent(studentDTO);
            return ResponseEntity.ok(savedStudent);
        } catch (Exception e) {
            // Log el error para debugging, pero retorna respuesta exitosa
            System.err.println("Error al guardar estudiante: " + e.getMessage());
            
            // Intenta obtener el estudiante existente para retornarlo
            try {
                Student existingStudent = studentService.findByEmail(studentDTO.getEmail());
                if (existingStudent != null) {
                    return ResponseEntity.ok(existingStudent);
                }
            } catch (Exception ex) {
                // Si no se puede obtener el estudiante existente, retorna error
                return ResponseEntity.badRequest().build();
            }
            
            return ResponseEntity.badRequest().build();
        }
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
        // Asegurar que el DTO tenga el ID correcto
        studentDTO.setId(id);
        Student updatedStudent = studentService.saveStudent(studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }
    
}
