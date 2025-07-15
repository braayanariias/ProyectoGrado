package com.example.proyectogrado.Controllers;

import com.example.proyectogrado.Models.Student;
import com.example.proyectogrado.Models.DTOs.ExerciseResponseDTO;
import com.example.proyectogrado.Models.DTOs.StudentDTO;
import com.example.proyectogrado.Services.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "API para interacción con el sistema de chat IA")
public class ChatController {

    private final ChatService chatService;

    ChatController (ChatService chatService) {
        this.chatService = chatService;
    };

    @PostMapping("/send")
    @Operation(summary = "Enviar mensaje al chat", 
              description = "Envía información del estudiante al chat IA para generar ejercicios personalizados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ejercicio generado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del estudiante inválidos"),
        @ApiResponse(responseCode = "500", description = "Error en el servicio de IA")
    })
    public Mono<ExerciseResponseDTO> sendMessage(@RequestBody StudentDTO studentDTO) {
        // Convertir DTO a entidad Student
        Student student = new Student();
        student.setId(studentDTO.getId()); // UUID de Supabase
        student.setFullName(studentDTO.getFullName());
        student.setEmail(studentDTO.getEmail());
        
        return chatService.sendMessage(student);
    }

}
