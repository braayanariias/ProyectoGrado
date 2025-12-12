package com.example.proyectogrado.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationPrompt {

    private String basePrompt = """
            Por favor evalúa la siguiente solución de código de un estudiante.
            
            EJERCICIO ORIGINAL:
            {exerciseContent}
            
            CÓDIGO ENVIADO POR EL ESTUDIANTE:
            {studentCode}
            
            INSTRUCCIONES:
            1. Analiza si el código resuelve correctamente el problema planteado
            2. Evalúa la calidad del código (sintaxis, lógica, buenas prácticas)
            3. Proporciona feedback constructivo
            4. Asigna una nota del 0 al 5 (puedes usar decimales como 3.5, 4.2, etc.) donde:
               - 0.0-1.0: Muy deficiente (no funciona o está muy mal)
               - 1.5-2.5: Deficiente (funciona parcialmente con errores importantes)
               - 3.0-3.5: Regular (funciona pero con errores menores o puede mejorar)
               - 4.0-4.5: Bueno (funciona bien con algunas mejoras menores)
               - 5.0: Excelente (funciona perfectamente y está bien escrito)
            
            FORMATO DE RESPUESTA REQUERIDO:
            NOTA: [número del 0 al 5, puede incluir decimales como 4.5]
            FEEDBACK: [tu análisis y recomendaciones detalladas]""";

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
