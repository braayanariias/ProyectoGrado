package com.example.proyectogrado.Models.DTOs;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    
    private UUID id; // UUID generado por Supabase
    private String fullName;
    private String email;
    
}
