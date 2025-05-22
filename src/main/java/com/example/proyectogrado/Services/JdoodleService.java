package com.example.proyectogrado.Services;


import com.example.proyectogrado.Models.Jdoodle;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class JdoodleService {

    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret;
    private final String apiUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JdoodleService(
            WebClient.Builder webClientBuilder,
            @Value("${jdoodle.client.id}") String clientId,
            @Value("${jdoodle.client.secret}") String clientSecret,
            @Value("${jdoodle.api.url}") String apiUrl
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.apiUrl = apiUrl;
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    /**
     * Ejecuta código en JDoodle y retorna la respuesta como String (output, errores, etc).
     */
    public Mono<String> executeCode(String script, String stdin, String language, String versionIndex, boolean compileOnly) {
        Jdoodle jdoodleRequest = new Jdoodle();
        jdoodleRequest.setClientId(clientId);
        jdoodleRequest.setClientSecret(clientSecret);
        jdoodleRequest.setScript(script);
        jdoodleRequest.setStdin(stdin);
        jdoodleRequest.setLanguage(language);
        jdoodleRequest.setVersionIndex(versionIndex);
        jdoodleRequest.setCompileOnly(compileOnly ? "true" : "false");

        try {
            String requestBodyJson = objectMapper.writeValueAsString(jdoodleRequest);
            return webClient.post()
                    .uri("/v1/execute")
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBodyJson)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(this::extractOutput);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Error al generar el JSON del request para JDoodle", e));
        }
    }

    /**
     * Extrae el output de la respuesta de JDoodle.
     */
    private String extractOutput(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            if (rootNode.has("output")) {
                return rootNode.get("output").asText();
            }
            return response;
        } catch (Exception e) {
            return "Error al procesar la respuesta de JDoodle: " + e.getMessage();
        }
    }
}
