package com.example.proyectogrado.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prompt {

    private String prompt = "Genera un ejercicio de lógica con la misma estructura y parecido a los ejercicios de adventJS, el ejercicio sera para ser desarrollado en Java que sea fácil de realizar. El ejercicio debe cumplir con los siguientes requisitos:1.Debe ser adecuado para principiantes en programación.2.Debe involucrar el uso de estructuras básicas como condicionales (if-else) y bucles (for o while).3.El problema debe ser claro y tener una solución directa.5.No me envies la solucion, yo te enviare la solucion y tu me daras una calificacion que va del 1.0 al 5.0 donde 1 es muy mala la solucion y 5 exelente, ademas puedes darme recomendaciones de como mejorar mi codigo";
    
}
