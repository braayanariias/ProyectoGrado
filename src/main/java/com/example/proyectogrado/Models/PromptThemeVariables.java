package com.example.proyectogrado.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromptThemeVariables {

    private String prompt = """
            Crea un ejercicio de programación para estudiantes principiantes cuyo objetivo principal sea practicar el uso de variables.
            
            El ejercicio debe:
            
            Incluir una historia breve o un contexto llamativo.
            
            Pedir al estudiante que declare y use al menos tres variables de distintos tipos (por ejemplo: int, double, String).
            
            Involucrar operaciones simples como suma, concatenación o asignación.
            
            Incluir una muestra del resultado esperado cuando se ejecuta correctamente.
            
            El enunciado debe ser claro, motivador y comprensible para alguien que está aprendiendo a programar por primera vez.
            
            Formato de salida:
            
            Título del ejercicio
            
            Descripción del problema
            
            Requisitos técnicos
            
            Ejemplo de entrada y salida esperada (en pseudocódigo o como impresión por consola)
            
            Nivel de dificultad: Básico""";

}
