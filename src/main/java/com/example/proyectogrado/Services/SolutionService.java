package com.example.proyectogrado.Services;

import com.example.proyectogrado.Models.Solution;
import com.example.proyectogrado.Models.Exercise;
import com.example.proyectogrado.Models.Student;
import com.example.proyectogrado.Models.DTOs.SolutionSubmissionDTO;
import com.example.proyectogrado.Models.DTOs.SolutionResponseDTO;
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

    public SolutionService(SolutionRepository solutionRepository, 
                          ExerciseService exerciseService, 
                          StudentService studentService,
                          ChatService chatService) {
        this.solutionRepository = solutionRepository;
        this.exerciseService = exerciseService;
        this.studentService = studentService;
        this.chatService = chatService;
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

        // Crear la solución
        Solution solution = new Solution();
        solution.setCode(submissionDTO.getCode());
        solution.setExercise(exercise);
        solution.setStudent(student);
        solution.setIsEvaluated(false);

        // Guardar la solución antes de evaluarla
        Solution savedSolution = solutionRepository.save(solution);

        // Marcar el ejercicio como completado
        exerciseService.markAsCompleted(exercise.getId());

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
        StringBuilder prompt = new StringBuilder();
        prompt.append("Por favor evalúa la siguiente solución de código de un estudiante.\n\n");
        prompt.append("EJERCICIO ORIGINAL:\n");
        prompt.append(solution.getExercise().getExerciseContent());
        prompt.append("\n\nCÓDIGO ENVIADO POR EL ESTUDIANTE:\n");
        prompt.append(solution.getCode());
        prompt.append("\n\nINSTRUCCIONES:\n");
        prompt.append("1. Analiza si el código resuelve correctamente el problema planteado\n");
        prompt.append("2. Evalúa la calidad del código (sintaxis, lógica, buenas prácticas)\n");
        prompt.append("3. Proporciona feedback constructivo\n");
        prompt.append("4. Asigna una nota del 1 al 5 donde:\n");
        prompt.append("   - 1: Muy deficiente (no funciona o está muy mal)\n");
        prompt.append("   - 2: Deficiente (funciona parcialmente con errores importantes)\n");
        prompt.append("   - 3: Regular (funciona pero con errores menores o puede mejorar)\n");
        prompt.append("   - 4: Bueno (funciona bien con algunas mejoras menores)\n");
        prompt.append("   - 5: Excelente (funciona perfectamente y está bien escrito)\n\n");
        prompt.append("FORMATO DE RESPUESTA REQUERIDO:\n");
        prompt.append("NOTA: [número del 1 al 5]\n");
        prompt.append("FEEDBACK: [tu análisis y recomendaciones detalladas]");
        
        return prompt.toString();
    }

    private void parseGeminiEvaluation(Solution solution, String geminiResponse) {
        try {
            // Buscar la nota
            String[] lines = geminiResponse.split("\n");
            Integer grade = null;
            StringBuilder feedback = new StringBuilder();
            boolean feedbackStarted = false;

            for (String line : lines) {
                if (line.trim().startsWith("NOTA:")) {
                    String gradeStr = line.replace("NOTA:", "").trim();
                    try {
                        grade = Integer.parseInt(gradeStr);
                        if (grade < 1 || grade > 5) {
                            grade = 3; // Valor por defecto si está fuera del rango
                        }
                    } catch (NumberFormatException e) {
                        grade = 3; // Valor por defecto si no se puede parsear
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
                grade = 3;
            }

            // Si no se encontró feedback específico, usar toda la respuesta
            if (feedback.length() == 0) {
                feedback.append(geminiResponse);
            }

            solution.setGrade(grade);
            solution.setFeedback(feedback.toString().trim());

        } catch (Exception e) {
            // En caso de error al parsear, usar valores por defecto
            solution.setGrade(3);
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
        List<Solution> solutions = solutionRepository.findByExerciseId(exerciseId);
        return solutions.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<SolutionResponseDTO> getSolutionById(UUID solutionId) {
        Optional<Solution> solution = solutionRepository.findById(solutionId);
        return solution.map(this::convertToResponseDTO);
    }

    public List<SolutionResponseDTO> getUnevaluatedSolutions() {
        List<Solution> solutions = solutionRepository.findByIsEvaluated(false);
        return solutions.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<SolutionResponseDTO> getLatestSolutionByExerciseAndStudent(UUID exerciseId, String studentEmail) {
        Student student = studentService.findByEmail(studentEmail);
        if (student == null) {
            return Optional.empty();
        }

        Optional<Exercise> exerciseOpt = exerciseService.getExerciseById(exerciseId);
        if (exerciseOpt.isEmpty()) {
            return Optional.empty();
        }

        Optional<Solution> solution = solutionRepository.findFirstByExerciseAndStudentOrderBySubmittedDateDesc(
                exerciseOpt.get(), student);
        
        return solution.map(this::convertToResponseDTO);
    }

    private SolutionResponseDTO convertToResponseDTO(Solution solution) {
        SolutionResponseDTO dto = new SolutionResponseDTO();
        dto.setId(solution.getId());
        dto.setCode(solution.getCode());
        dto.setFeedback(solution.getFeedback());
        dto.setGrade(solution.getGrade());
        dto.setSubmittedDate(solution.getSubmittedDate());
        dto.setEvaluatedDate(solution.getEvaluatedDate());
        dto.setIsEvaluated(solution.getIsEvaluated());
        
        if (solution.getExercise() != null) {
            dto.setExerciseId(solution.getExercise().getId());
            dto.setExerciseContent(solution.getExercise().getExerciseContent());
        }
        
        if (solution.getStudent() != null) {
            dto.setStudentName(solution.getStudent().getFullName());
            dto.setStudentEmail(solution.getStudent().getEmail());
        }
        
        return dto;
    }
}
