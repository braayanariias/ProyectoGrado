package com.example.proyectogrado.Services;

import com.example.proyectogrado.Models.Solution;
import com.example.proyectogrado.Models.Exercise;
import com.example.proyectogrado.Models.Student;
import com.example.proyectogrado.Models.EvaluationPrompt;
import com.example.proyectogrado.Models.DTOs.SolutionSubmissionDTO;
import com.example.proyectogrado.Models.DTOs.SolutionResponseDTO;
import com.example.proyectogrado.Models.DTOs.JDoodleResponseDTO;
import com.example.proyectogrado.Exceptions.CodeCompilationException;
import com.example.proyectogrado.Repositorys.SolutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SolutionService {

    private final SolutionRepository solutionRepository;
    private final ExerciseService exerciseService;
    private final StudentService studentService;
    private final ChatService chatService;
    private final JDoodleService jDoodleService;
    private final EvaluationPrompt evaluationPrompt;

    public SolutionService(SolutionRepository solutionRepository, 
                          ExerciseService exerciseService, 
                          StudentService studentService,
                          ChatService chatService,
                          JDoodleService jDoodleService) {
        this.solutionRepository = solutionRepository;
        this.exerciseService = exerciseService;
        this.studentService = studentService;
        this.chatService = chatService;
        this.jDoodleService = jDoodleService;
        this.evaluationPrompt = new EvaluationPrompt();
    }

    @Transactional
    public SolutionResponseDTO submitSolution(SolutionSubmissionDTO submissionDTO) {
        // Buscar el ejercicio
        Optional<Exercise> exerciseOpt = exerciseService.getExerciseById(submissionDTO.getExerciseId());
        if (exerciseOpt.isEmpty()) {
            throw new RuntimeException("Exercise not found with id: " + submissionDTO.getExerciseId());
        }

        // Buscar el estudiante
        Student student = studentService.findByEmail(submissionDTO.getStudentEmail());
        if (student == null) {
            throw new RuntimeException("Student not found with email: " + submissionDTO.getStudentEmail());
        }

        Exercise exercise = exerciseOpt.get();

        // Verificar que el ejercicio pertenece al estudiante
        if (!exercise.getStudent().getId().equals(student.getId())) {
            throw new RuntimeException("Exercise does not belong to the student");
        }

        // VALIDACIÓN CON JDOODLE ANTES DE GUARDAR (solo compilación)
        JDoodleResponseDTO compilationResult = jDoodleService.validateJavaCodeCompileOnly(submissionDTO.getCode());
        
        // Si el código no compila, lanzar excepción con detalles de compilación
        if (!compilationResult.isCompiled()) {
            String errorMessage = "El código no compila correctamente";
            String compilationError = compilationResult.getError() != null ? 
                                    compilationResult.getError() : 
                                    compilationResult.getOutput();
            throw new CodeCompilationException(errorMessage, compilationError, compilationResult.getOutput());
        }

        // Si llegamos aquí, el código compila correctamente, procedemos a guardarlo
        Solution solution = new Solution();
        solution.setCode(submissionDTO.getCode());
        solution.setExercise(exercise);
        solution.setStudent(student);
        solution.setIsEvaluated(false);

        // Guardar la solución
        Solution savedSolution = solutionRepository.save(solution);

        // Marcar el ejercicio como completado
        exerciseService.markAsCompleted(exercise.getExerciseId());

        // Evaluar el código con Gemini
        try {
            evaluateSolutionWithGemini(savedSolution);
        } catch (Exception e) {
            // Si falla la evaluación, marcamos como no evaluada pero guardamos la solución
            System.err.println("Error evaluating solution with Gemini: " + e.getMessage());
        }

        return convertToResponseDTO(savedSolution);
    }

    @Transactional
    public void evaluateSolutionWithGemini(Solution solution) {
        try {
            // Crear el prompt para Gemini
            String evaluationPrompt = createEvaluationPrompt(solution);
            
            // Llamar a Gemini para evaluar
            String geminiResponse = chatService.generateResponse(evaluationPrompt);
            
            // Parsear la respuesta de Gemini
            parseGeminiEvaluation(solution, geminiResponse);
            
            solution.setIsEvaluated(true);
            solution.setEvaluatedDate(LocalDateTime.now());
            
            solutionRepository.save(solution);
            
        } catch (Exception e) {
            throw new RuntimeException("Error evaluating solution with Gemini: " + e.getMessage(), e);
        }
    }

    private String createEvaluationPrompt(Solution solution) {
        return evaluationPrompt.generateEvaluationPrompt(
                solution.getExercise().getExerciseContent(),
                solution.getCode()
        );
    }

    private void parseGeminiEvaluation(Solution solution, String geminiResponse) {
        try {
            // Buscar la nota
            String[] lines = geminiResponse.split("\n");
            Double grade = null;
            StringBuilder feedback = new StringBuilder();
            boolean feedbackStarted = false;

            for (String line : lines) {
                if (line.trim().startsWith("NOTA:")) {
                    String gradeStr = line.replace("NOTA:", "").trim();
                    try {
                        grade = Double.parseDouble(gradeStr);
                        if (grade < 0.0 || grade > 5.0) {
                            grade = 3.0; // Valor por defecto si está fuera del rango
                        }
                    } catch (NumberFormatException e) {
                        grade = 3.0; // Valor por defecto si no se puede parsear
                    }
                } else if (line.trim().startsWith("FEEDBACK:")) {
                    feedbackStarted = true;
                    feedback.append(line.replace("FEEDBACK:", "").trim());
                } else if (feedbackStarted) {
                    feedback.append("\n").append(line);
                }
            }

            // Si no se encontró nota, usar valor por defecto
            if (grade == null) {
                grade = 3.0;
            }

            // Si no se encontró feedback específico, usar toda la respuesta
            if (feedback.length() == 0) {
                feedback.append(geminiResponse);
            }

            solution.setGrade(grade);
            solution.setFeedback(feedback.toString().trim());

        } catch (Exception e) {
            // En caso de error al parsear, usar valores por defecto
            solution.setGrade(3.0);
            solution.setFeedback("Evaluación completada. " + geminiResponse);
        }
    }

    public List<SolutionResponseDTO> getSolutionsByStudentEmail(String email) {
        List<Solution> solutions = solutionRepository.findByStudentEmail(email);
        return solutions.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SolutionResponseDTO> getSolutionsByExerciseId(UUID exerciseId) {
        List<Solution> solutions = solutionRepository.findSolutionsByExerciseId(exerciseId);
        return solutions.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<SolutionResponseDTO> getSolutionById(UUID solutionId) {
        Optional<Solution> solution = solutionRepository.findById(solutionId);
        return solution.map(this::convertToResponseDTO);
    }

    private SolutionResponseDTO convertToResponseDTO(Solution solution) {
        SolutionResponseDTO dto = new SolutionResponseDTO();
        dto.setSolutionId(solution.getSolutionId());
        dto.setCode(solution.getCode());
        dto.setFeedback(solution.getFeedback());
        dto.setGrade(solution.getGrade());
        dto.setSubmittedDate(solution.getSubmittedDate());
        dto.setEvaluatedDate(solution.getEvaluatedDate());
        dto.setIsEvaluated(solution.getIsEvaluated());
        
        if (solution.getExercise() != null) {
            dto.setExerciseId(solution.getExercise().getExerciseId());
            dto.setExerciseContent(solution.getExercise().getExerciseContent());
        }
        
        if (solution.getStudent() != null) {
            dto.setStudentName(solution.getStudent().getFullName());
            dto.setStudentEmail(solution.getStudent().getEmail());
        }
        
        return dto;
    }

    public List<Solution> getSolutionsByExerciseIdAndStudentId(UUID exerciseId, UUID studentId) {
        return solutionRepository.findSolutionsByExercise_ExerciseIdAndStudent_Id(exerciseId, studentId);
    }

}
