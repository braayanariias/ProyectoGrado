package com.example.proyectogrado.Exceptions;

import org.hibernate.StaleObjectStateException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.OptimisticLockException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, String>> handleOptimisticLockingFailure(OptimisticLockingFailureException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "CONCURRENT_MODIFICATION");
        errorResponse.put("message", "Los datos fueron modificados por otra transacción. Por favor, actualice los datos e intente nuevamente.");
        errorResponse.put("details", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<Map<String, String>> handleOptimisticLockException(OptimisticLockException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "CONCURRENT_MODIFICATION");
        errorResponse.put("message", "Los datos fueron modificados por otra transacción. Por favor, actualice los datos e intente nuevamente.");
        errorResponse.put("details", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(StaleObjectStateException.class)
    public ResponseEntity<Map<String, String>> handleStaleObjectState(StaleObjectStateException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "STALE_OBJECT_STATE");
        errorResponse.put("message", "El objeto fue actualizado o eliminado por otra transacción. Por favor, refresque los datos e intente nuevamente.");
        errorResponse.put("details", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CodeCompilationException.class)
    public ResponseEntity<Map<String, String>> handleCodeCompilationException(CodeCompilationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "COMPILATION_ERROR");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("compilationError", ex.getCompilationError());
        errorResponse.put("jdoodleOutput", ex.getJdoodleOutput());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "INTERNAL_SERVER_ERROR");
        errorResponse.put("message", "Ha ocurrido un error interno del servidor.");
        errorResponse.put("details", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
