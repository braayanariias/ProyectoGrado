package com.example.proyectogrado.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prompt {

    private String prompt = """
            Genera un ejercicio de lógica de programación diseñado para estudiantes principiantes en Java.
            El ejercicio debe seguir el estilo de los desafíos de AdventJS: problemas prácticos, divertidos y con contexto narrativo.
            
            CRITERIOS DEL EJERCICIO:
            1. Nivel: Adecuado para principiantes en programación
            2. Estilo AdventJS: Debe tener una narrativa o contexto temático interesante (navidad, aventuras, situaciones cotidianas, etc.)
            3. Estructuras requeridas: Debe incluir el uso de estructuras básicas como:
               - Condicionales (if-else, switch)
               - Bucles (for, while, do-while)
               - Variables y operadores básicos
            4. Enunciado: Claro y preciso con descripción detallada del problema
            5. Ejemplos: Incluye al menos 2-3 ejemplos de entrada y salida esperada
            6. Dificultad: Progresiva, que permita al estudiante aplicar conceptos básicos
            
            FORMATO DE RESPUESTA:
            - Título del ejercicio
            - Descripción del problema
            - Requisitos específicos
            - Ejemplos de entrada y salida
            - Restricciones (si las hay)
            
            IMPORTANTE: NO incluyas la solución en tu respuesta, solo el enunciado del ejercicio.
            """;
    
}
