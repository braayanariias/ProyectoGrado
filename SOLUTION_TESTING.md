# Test de Endpoints para Sistema de Soluciones

## Ejemplo de flujo completo

### 1. Primero, sincronizar un estudiante
```bash
curl -X POST http://localhost:8080/api/student/sync-from-supabase \
  -H "Content-Type: application/json" \
  -d '{
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "test.student@example.com",
    "fullName": "Estudiante de Prueba"
  }'
```

### 2. Generar un ejercicio para el estudiante
```bash
curl -X POST http://localhost:8080/api/chat/send \
  -H "Content-Type: application/json" \
  -d '{
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "test.student@example.com",
    "fullName": "Estudiante de Prueba"
  }'
```

### 3. Obtener los ejercicios del estudiante
```bash
curl -X GET http://localhost:8080/api/exercises/student/email/test.student@example.com
```

### 4. Enviar una solución de código (usar el ID del ejercicio obtenido en el paso 3)
```bash
curl -X POST http://localhost:8080/api/solutions/submit \
  -H "Content-Type: application/json" \
  -d '{
    "exerciseId": "[REEMPLAZAR_CON_ID_DEL_EJERCICIO]",
    "studentEmail": "test.student@example.com",
    "code": "function fibonacci(n) {\n    if (n <= 1) return n;\n    return fibonacci(n-1) + fibonacci(n-2);\n}"
  }'
```

### 5. Obtener todas las soluciones del estudiante
```bash
curl -X GET http://localhost:8080/api/solutions/student/email/test.student@example.com
```

### 6. Obtener la última solución para un ejercicio específico
```bash
curl -X GET http://localhost:8080/api/solutions/latest/exercise/[ID_EJERCICIO]/student/test.student@example.com
```

### 7. Obtener todas las soluciones de un ejercicio
```bash
curl -X GET http://localhost:8080/api/solutions/exercise/[ID_EJERCICIO]
```

### 8. Marcar ejercicio como completado
```bash
curl -X PUT http://localhost:8080/api/exercises/[ID_EJERCICIO]/complete
```

## Notas importantes:
- Reemplaza [REEMPLAZAR_CON_ID_DEL_EJERCICIO] y [ID_EJERCICIO] con los IDs reales obtenidos de las respuestas
- La evaluación con Gemini se ejecuta automáticamente al enviar la solución
- La nota y feedback se generan automáticamente y se almacenan en la base de datos
- Cada estudiante puede enviar múltiples soluciones para el mismo ejercicio
