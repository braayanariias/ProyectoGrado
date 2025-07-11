package com.example.proyectogrado.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationPrompt {

    private String basePrompt = "Por favor evalúa la siguiente solución de código de un estudiante.\n\n" +
            "EJERCICIO ORIGINAL:\n" +
            "{exerciseContent}\n\n" +
            "CÓDIGO ENVIADO POR EL ESTUDIANTE:\n" +
            "{studentCode}\n\n" +
            "INSTRUCCIONES:\n" +
            "1. Analiza si el código resuelve correctamente el problema planteado\n" +
            "2. Evalúa la calidad del código (sintaxis, lógica, buenas prácticas)\n" +
            "3. Proporciona feedback constructivo\n" +
            "4. Asigna una nota del 0 al 5 (puedes usar decimales como 3.5, 4.2, etc.) donde:\n" +
            "   - 0.0-1.0: Muy deficiente (no funciona o está muy mal)\n" +
            "   - 1.5-2.5: Deficiente (funciona parcialmente con errores importantes)\n" +
            "   - 3.0-3.5: Regular (funciona pero con errores menores o puede mejorar)\n" +
            "   - 4.0-4.5: Bueno (funciona bien con algunas mejoras menores)\n" +
            "   - 5.0: Excelente (funciona perfectamente y está bien escrito)\n\n" +
            "FORMATO DE RESPUESTA REQUERIDO:\n" +
            "NOTA: [número del 0 al 5, puede incluir decimales como 4.5]\n" +
            "FEEDBACK: [tu análisis y recomendaciones detalladas]";

    /**
     * Genera el prompt completo de evaluación reemplazando los placeholders
     * con el contenido del ejercicio y el código del estudiante
     */
    public String generateEvaluationPrompt(String exerciseContent, String studentCode) {
        return basePrompt
                .replace("{exerciseContent}", exerciseContent)
                .replace("{studentCode}", studentCode);
    }
}
