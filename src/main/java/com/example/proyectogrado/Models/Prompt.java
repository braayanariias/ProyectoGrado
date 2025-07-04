package com.example.proyectogrado.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prompt {

    private String prompt = "Genera un ejercicio de lógica similar en estilo y dificultad a los de adventJS. El ejercicio debe estar diseñado para ser resuelto en Java y cumplir con los siguientes criterios: Debe ser adecuado para principiantes en programación. Debe incluir el uso de estructuras básicas como condicionales (if-else) y bucles (for o while). El enunciado debe ser claro, con una descripción precisa del problema y una solución directa. No incluyas la solución en tu respuesta. Después de que yo te envíe mi solución en Java, evalúala con una calificación entre 1.0 y 5.0, donde: 1.0 significa \"muy deficiente\" 5.0 significa \"excelente\"\n\nAdemás, proporciona retroalimentación específica sobre cómo podría mejorar mi código en aspectos como legibilidad, eficiencia y buenas prácticas.";
    
}
