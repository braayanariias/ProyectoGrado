package com.example.proyectogrado.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseResponseDTO {
    private UUID exerciseId;
    private String exerciseContent;
}
