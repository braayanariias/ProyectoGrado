package com.example.proyectogrado.Exceptions;

public class CodeCompilationException extends RuntimeException {
    
    private final String compilationError;
    private final String jdoodleOutput;

    public CodeCompilationException(String message, String compilationError, String jdoodleOutput) {
        super(message);
        this.compilationError = compilationError;
        this.jdoodleOutput = jdoodleOutput;
    }

    public String getCompilationError() {
        return compilationError;
    }

    public String getJdoodleOutput() {
        return jdoodleOutput;
    }
}
