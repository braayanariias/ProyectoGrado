-- Script para crear la base de datos y tabla students
-- Ejecuta este script en MySQL antes de ejecutar la aplicación

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS proyecto;

-- Usar la base de datos
USE proyecto;

-- Crear la tabla students si no existe
CREATE TABLE IF NOT EXISTS students (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    full_name VARCHAR(255) NOT NULL
);

-- Verificar que la tabla se creó correctamente
DESCRIBE students;
