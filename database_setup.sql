-- Script para crear la base de datos y tablas necesarias
-- Ejecuta este script en MySQL antes de ejecutar la aplicación

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS proyecto;

-- Usar la base de datos
USE proyecto;

-- Crear la tabla students si no existe
CREATE TABLE IF NOT EXISTS students (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    version BIGINT DEFAULT 0
);

-- Crear la tabla exercises si no existe
CREATE TABLE IF NOT EXISTS exercises (
    id VARCHAR(36) PRIMARY KEY,
    exercise_content TEXT NOT NULL,
    assigned_date DATETIME NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    student_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

-- Verificar que las tablas se crearon correctamente
DESCRIBE students;
DESCRIBE exercises;
