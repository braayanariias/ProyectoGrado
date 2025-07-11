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

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    ChatController (ChatService chatService) {
        this.chatService = chatService;
    };

    @PostMapping("/send")
    public Mono<ExerciseResponseDTO> sendMessage(@RequestBody StudentDTO studentDTO) {
        // Convertir DTO a entidad Student
        Student student = new Student();
        student.setId(studentDTO.getId()); // UUID de Supabase
        student.setFullName(studentDTO.getFullName());
        student.setEmail(studentDTO.getEmail());
        
        return chatService.sendMessage(student);
    }

}
