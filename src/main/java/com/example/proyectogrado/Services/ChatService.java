package com.example.proyectogrado.Services;

import com.example.proyectogrado.Models.ChatMessage;
import com.example.proyectogrado.Models.Prompt;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final WebClient webClient;
    private final List<ChatMessage> conversationHistory = new ArrayList<>();
    private final Prompt prompt = new Prompt();
    private final String API_KEY;
    private final ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    public ChatService(
            WebClient.Builder webClientBuilder,
            @Value("${gemini.api.url}") String apiUrl,
            @Value("${gemini.api.key}") String apiKey) {
        this.API_KEY = apiKey;
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    // Envía el mensaje al modelo de IA y devuelve la respuesta
    public Mono<String> sendMessage() {
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
                    .map(this::extractMessage);
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

    public static Map<String, String> extractTitleAndContent(String ejercicio) {
        Map<String, String> result = new HashMap<>();
        String title = "";
        String content = "";

        // Buscar el primer título entre ** **
        int start = ejercicio.indexOf("**");
        int end = ejercicio.indexOf("**", start + 2);

        if (start != -1 && end != -1) {
            title = ejercicio.substring(start + 2, end).trim();
            content = ejercicio.substring(end + 2).trim();
        } else {
            // Si no encuentra el formato, todo es contenido
            content = ejercicio;
        }

        result.put("title", title);
        result.put("content", content);
        return result;
    }

}
