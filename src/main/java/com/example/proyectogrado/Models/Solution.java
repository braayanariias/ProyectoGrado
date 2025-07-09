package com.example.proyectogrado.Models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solutions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "grade")
    private Integer grade; // Nota del 1 al 5

    @Column(name = "submitted_date")
    private LocalDateTime submittedDate;

    @Column(name = "evaluated_date")
    private LocalDateTime evaluatedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private Student student;

    @Column(name = "is_evaluated")
    private Boolean isEvaluated = false;

    @PrePersist
    protected void onCreate() {
        submittedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        if (isEvaluated && evaluatedDate == null) {
            evaluatedDate = LocalDateTime.now();
        }
    }
}
