package com.example.proyectogrado.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolutionResponseDTO {
    private UUID id;
    private String code;
    private String feedback;
    private Integer grade;
    private LocalDateTime submittedDate;
    private LocalDateTime evaluatedDate;
    private UUID exerciseId;
    private String exerciseContent;
    private String studentName;
    private String studentEmail;
    private Boolean isEvaluated;
}
