package com.example.proyectogrado.Repositorys;

import com.example.proyectogrado.Models.Student;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,UUID> {
    Optional<Student> findByEmail(String email);
}
