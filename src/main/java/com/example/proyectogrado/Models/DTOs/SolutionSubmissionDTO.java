package com.example.proyectogrado.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolutionSubmissionDTO {
    private UUID exerciseId;
    private String studentEmail;
    private String code;
}
