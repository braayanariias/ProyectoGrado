package com.example.proyectogrado.Controllers;

import com.example.proyectogrado.Services.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
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
    public Mono<String> sendMessage() {
        return chatService.sendMessage();
    }

}
