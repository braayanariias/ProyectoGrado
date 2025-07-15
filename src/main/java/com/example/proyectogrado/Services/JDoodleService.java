package com.example.proyectogrado.Services;

import com.example.proyectogrado.Models.DTOs.JDoodleRequestDTO;
import com.example.proyectogrado.Models.DTOs.JDoodleResponseDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class JDoodleService {

    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret;
    private final ObjectMapper objectMapper;

    public JDoodleService(
            WebClient.Builder webClientBuilder,
            @Value("${jdoodle.api.url}") String apiUrl,
            @Value("${jdoodle.client.id}") String clientId,
            @Value("${jdoodle.client.secret}") String clientSecret) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        
        // Configurar ObjectMapper para ignorar campos desconocidos
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Valida si el código Java compila correctamente (solo compilación, no ejecución)
     * @param javaCode El código Java a validar
     * @return JDoodleResponseDTO con información de compilación
     */
    public JDoodleResponseDTO validateJavaCodeCompileOnly(String javaCode) {
        try {
            // Crear la petición para JDoodle con compileOnly = true
            JDoodleRequestDTO requestDTO = new JDoodleRequestDTO(
                    clientId,
                    clientSecret,
                    javaCode,
                    "java",
                    "4",  // Java 8 por defecto
                    true  // Solo compilar, no ejecutar
            );

            // Convertir a JSON
            String requestJson = objectMapper.writeValueAsString(requestDTO);

            // Realizar la petición
            String response = webClient.post()
                    .uri("/v1/execute")
                    .header("Content-Type", "application/json")
                    .bodyValue(requestJson)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(); // Bloquear para obtener respuesta síncrona

            // Parsear la respuesta
            return objectMapper.readValue(response, JDoodleResponseDTO.class);

        } catch (Exception e) {
            // En caso de error, crear una respuesta que indique que no compiló
            JDoodleResponseDTO errorResponse = new JDoodleResponseDTO();
            errorResponse.setCompiled(false);
            errorResponse.setError("Error al validar código con JDoodle: " + e.getMessage());
            errorResponse.setCompilationStatus("Error de conexión");
            return errorResponse;
        }
    }
}
