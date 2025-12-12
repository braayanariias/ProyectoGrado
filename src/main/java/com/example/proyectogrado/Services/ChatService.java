package com.example.proyectogrado.Services;

import com.example.proyectogrado.Models.*;
import com.example.proyectogrado.Models.DTOs.ExerciseResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final WebClient webClient;
    private final Prompt prompt = new Prompt();
    private final String API_KEY;
    private final ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    private final ExerciseService exerciseService;

    public ChatService(
            WebClient.Builder webClientBuilder,
            @Value("${gemini.api.url}") String apiUrl,
            @Value("${gemini.api.key}") String apiKey,
            StudentService studentService,
            ExerciseService exerciseService) {
        this.API_KEY = apiKey;
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.exerciseService = exerciseService;
    }

    // Envía el mensaje al modelo de IA y devuelve la respuesta
    public Mono<ExerciseResponseDTO> sendMessage(Student student) {
        // Crear el mensaje del usuario
        ChatMessage.Part part = new ChatMessage.Part();
        part.setText(prompt.getPrompt());

        ChatMessage userChatMessage = new ChatMessage();
        userChatMessage.setRole("user");
        userChatMessage.setParts(List.of(part));

        // Crear una conversación temporal solo para esta consulta
        List<ChatMessage> tempConversation = List.of(userChatMessage);
        Map<String, Object> requestBody = Map.of("contents", tempConversation);
        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            return webClient.post()
                    .uri("/gemini-2.5-flash:generateContent?key=" + API_KEY)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBodyJson)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(response -> {
                        String exerciseContent = extractMessage(response);
                        // Guardar el ejercicio en la base de datos relacionado con el estudiante
                        Exercise savedExercise = exerciseService.createAndSaveExercise(exerciseContent, student);
                        // Devolver tanto el ID como el contenido del ejercicio
                        return new ExerciseResponseDTO(savedExercise.getExerciseId(), exerciseContent);
                    });
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Error al generar el JSON del request", e));
        }
    }

    // Envía el mensaje al modelo de IA y devuelve la respuesta
    public Mono<ExerciseResponseDTO> sendMessageTheme(Student student, String theme) {
        // Crear el mensaje del usuario
        ChatMessage.Part part = new ChatMessage.Part();
        if ("variables".equalsIgnoreCase(theme)) {
            part.setText(new PromptThemeVariables().getPrompt());
        } else {
            part.setText(prompt.getPrompt());
        }

        ChatMessage userChatMessage = new ChatMessage();
        userChatMessage.setRole("user");
        userChatMessage.setParts(List.of(part));

        // Crear una conversación temporal solo para esta consulta
        List<ChatMessage> tempConversation = List.of(userChatMessage);
        Map<String, Object> requestBody = Map.of("contents", tempConversation);
        try {
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            return webClient.post()
                    .uri("/gemini-2.5-flash:generateContent?key=" + API_KEY)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBodyJson)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(response -> {
                        String exerciseContent = extractMessage(response);
                        // Guardar el ejercicio en la base de datos relacionado con el estudiante
                        Exercise savedExercise = exerciseService.createAndSaveExercise(exerciseContent, student);
                        // Devolver tanto el ID como el contenido del ejercicio
                        return new ExerciseResponseDTO(savedExercise.getExerciseId(), exerciseContent);
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

    // Método para generar respuestas generales (usado para evaluación de código)
    public String generateResponse(String message) {
        try {
            // Crear el mensaje del usuario
            ChatMessage.Part part = new ChatMessage.Part();
            part.setText(message);

            ChatMessage userChatMessage = new ChatMessage();
            userChatMessage.setRole("user");
            userChatMessage.setParts(List.of(part));

            // Crear una conversación temporal solo para esta consulta
            List<ChatMessage> tempConversation = List.of(userChatMessage);
            Map<String, Object> requestBody = Map.of("contents", tempConversation);
            
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            String response = webClient.post()
                    .uri("/gemini-2.5-flash:generateContent?key=" + API_KEY)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBodyJson)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Bloquear para obtener respuesta síncrona

            return extractMessage(response);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar respuesta con Gemini: " + e.getMessage(), e);
        }
    }

}
