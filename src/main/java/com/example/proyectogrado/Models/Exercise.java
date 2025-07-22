package com.example.proyectogrado.Models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exercises")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID exerciseId;

    @Column(columnDefinition = "TEXT")
    private String exerciseContent;

    @Column(name = "assigned_date")
    private LocalDateTime assignedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @JsonManagedReference
    private Student student;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @PrePersist
    protected void onCreate() {
        assignedDate = LocalDateTime.now();
    }
}
