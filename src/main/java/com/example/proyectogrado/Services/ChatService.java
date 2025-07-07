package com.example.proyectogrado.Services;

import com.example.proyectogrado.Models.ChatMessage;
import com.example.proyectogrado.Models.Exercise;
import com.example.proyectogrado.Models.Prompt;
import com.example.proyectogrado.Models.Student;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final WebClient webClient;
    private final List<ChatMessage> conversationHistory = new ArrayList<>();
    private final Prompt prompt = new Prompt();
    private final String API_KEY;
    private final ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    private final StudentService studentService;
    private final ExerciseService exerciseService;

    public ChatService(
            WebClient.Builder webClientBuilder,
            @Value("${gemini.api.url}") String apiUrl,
            @Value("${gemini.api.key}") String apiKey,
            StudentService studentService,
            ExerciseService exerciseService) {
        this.API_KEY = apiKey;
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.studentService = studentService;
        this.exerciseService = exerciseService;
    }

    // Envía el mensaje al modelo de IA y devuelve la respuesta
    public Mono<String> sendMessage(Student student) {
        // Guardar el estudiante en la base de datos
        studentService.saveStudent(student);
        
        // Agregar mensaje del usuario al historial
        ChatMessage.Part part = new ChatMessage.Part();
        part.setText(prompt.getPrompt());

        // Crear el mensaje del usuario
        ChatMessage userChatMessage = new ChatMessage();
        userChatMessage.setRole("user");
        userChatMessage.setParts(List.of(part));

        // Agregar el mensaje del usuario al historial
        conversationHistory.add(userChatMessage);
        Map<String, Object> requestBody = Map.of("contents", conversationHistory);
        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            return webClient.post()
                    .uri("/gemini-2.0-flash:generateContent?key=" + API_KEY)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBodyJson)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(response -> {
                        String exerciseContent = extractMessage(response);
                        // Guardar el ejercicio en la base de datos relacionado con el estudiante
                        exerciseService.createAndSaveExercise(exerciseContent, student);
                        return exerciseContent;
                    });
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Error al generar el JSON del request", e));
        }
    }

    // Extrae el mensaje de la respuesta del modelo de IA
    private String extractMessage(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode candidates = rootNode.path("candidates");
            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode firstCandidate = candidates.get(0);
                JsonNode content = firstCandidate.path("content");
                JsonNode parts = content.path("parts");
                if (parts.isArray() && !parts.isEmpty()) {
                    return parts.get(0).path("text").asText();
                }
            }
            return "No se pudo extraer la respuesta del modelo.";
        } catch (Exception e) {
            return "Error al procesar la respuesta del modelo: " + e.getMessage();
        }
    }

}
