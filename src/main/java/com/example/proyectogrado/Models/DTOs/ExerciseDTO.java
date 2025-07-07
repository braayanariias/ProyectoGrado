package com.example.proyectogrado.Models.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDTO {
    
    private UUID id;
    private String exerciseContent;
    private LocalDateTime assignedDate;
    private Boolean isCompleted;
    private UUID studentId;
    private String studentName;
    private String studentEmail;
}
