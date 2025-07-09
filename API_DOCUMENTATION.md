# API Documentation

## Arquitectura de Autenticación

Esta API está diseñada para trabajar con **Supabase** como sistema de autenticación principal. Los UUIDs de los estudiantes son generados por Supabase y sincronizados con nuestra base de datos local.

---

## Endpoints

### 1. Chat - Generar ejercicio de programación

#### POST `/api/chat/send`

Recibe información del estudiante autenticado desde Supabase, sincroniza los datos y genera un ejercicio de programación personalizado.

**Request Body (StudentDTO):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "estudiante@ejemplo.com",
  "fullName": "Juan Pérez"
}
```

**Response:**
```json
"Ejercicio de programación generado por la IA basado en el perfil del estudiante..."
```

**Ejemplo con curl:**
```bash
curl -X POST http://localhost:8080/api/chat/send \
  -H "Content-Type: application/json" \
  -d '{
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "estudiante@ejemplo.com",
    "fullName": "Juan Pérez"
  }'
```

---

### 2. Estudiantes - Crear/Actualizar

#### POST `/api/student/create`

Crea o actualiza un estudiante usando el UUID generado por Supabase.

**Request Body (StudentDTO):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "estudiante@ejemplo.com",
  "fullName": "Juan Pérez"
}
```

**Response (Student):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "estudiante@ejemplo.com",
  "fullName": "Juan Pérez",
  "version": 0
}
```

---

### 3. Estudiantes - Sincronización desde Supabase

#### POST `/api/student/sync-from-supabase`

Endpoint específico para sincronizar datos de estudiantes desde Supabase. Incluye validaciones adicionales.

**Request Body (StudentDTO):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "estudiante@ejemplo.com",
  "fullName": "Juan Pérez"
}
```

**Response (Student):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "estudiante@ejemplo.com",
  "fullName": "Juan Pérez",
  "version": 0
}
```

**Validaciones:**
- Todos los campos (`id`, `email`, `fullName`) son obligatorios
- El `id` debe ser un UUID válido generado por Supabase

---

### 4. Estudiantes - Actualizar por ID

#### PUT `/api/student/update/{id}`

Actualiza un estudiante específico usando su UUID.

**Path Parameters:**
- `id` (UUID): UUID del estudiante

**Request Body (StudentDTO):**
```json
{
  "email": "nuevo_email@ejemplo.com",
  "fullName": "Juan Carlos Pérez"
}
```

**Response (Student):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "nuevo_email@ejemplo.com",
  "fullName": "Juan Carlos Pérez",
  "version": 1
}
```

---

### 5. Estudiantes - Guardar directo (Legacy)

#### POST `/api/student/save`

Endpoint legacy que recibe directamente el objeto Student completo.

**Request Body (Student):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "estudiante@ejemplo.com",
  "fullName": "Juan Pérez"
}
```

---

### 6. Ejercicios - Gestión de ejercicios asignados

#### GET `/api/exercises/student/{studentId}`

Obtiene todos los ejercicios asignados a un estudiante específico ordenados por fecha de asignación (más recientes primero).

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "exerciseContent": "Ejercicio de programación generado...",
  "assignedDate": "2025-07-07T10:30:00",
  "isCompleted": false,
  "student": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "estudiante@ejemplo.com",
    "fullName": "Juan Pérez"
  }
}
```

#### GET `/api/exercises/student/email/{email}`

Obtiene todos los ejercicios de un estudiante usando su email.

#### GET `/api/exercises/pending`

Obtiene todos los ejercicios pendientes (no completados) de todos los estudiantes.

#### PUT `/api/exercises/{exerciseId}/complete`

Marca un ejercicio como completado.

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "exerciseContent": "Ejercicio de programación generado...",
  "assignedDate": "2025-07-07T10:30:00",
  "isCompleted": true,
  "student": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "email": "estudiante@ejemplo.com",
    "fullName": "Juan Pérez"
  }
}
```

#### GET `/api/exercises/{exerciseId}`

Obtiene los detalles de un ejercicio específico.

#### GET `/api/exercises/{exerciseId}/solutions`

Obtiene todas las soluciones enviadas para un ejercicio específico.

#### GET `/api/exercises/{exerciseId}/solutions/latest/student/{email}`

Obtiene la última solución enviada por un estudiante para un ejercicio específico.

---

### 7. Soluciones - Gestión de soluciones de código

#### POST `/api/solutions/submit`

Permite a un estudiante enviar una solución de código para un ejercicio. La solución será evaluada automáticamente por Gemini AI.

**Request Body (SolutionSubmissionDTO):**
```json
{
  "exerciseId": "550e8400-e29b-41d4-a716-446655440001",
  "studentEmail": "estudiante@ejemplo.com",
  "code": "function fibonacci(n) {\n  if (n <= 1) return n;\n  return fibonacci(n-1) + fibonacci(n-2);\n}"
}
```

**Response (SolutionResponseDTO):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440002",
  "code": "function fibonacci(n) {\n  if (n <= 1) return n;\n  return fibonacci(n-1) + fibonacci(n-2);\n}",
  "feedback": "Excelente implementación de la secuencia de Fibonacci usando recursión. El código es correcto y funcional. Para mejorar el rendimiento, podrías considerar usar programación dinámica o memoización para evitar cálculos repetidos.",
  "grade": 4,
  "submittedDate": "2025-07-09T14:30:00",
  "evaluatedDate": "2025-07-09T14:30:15",
  "exerciseId": "550e8400-e29b-41d4-a716-446655440001",
  "exerciseContent": "Implementa una función que calcule el n-ésimo número de la secuencia de Fibonacci...",
  "studentName": "Juan Pérez",
  "studentEmail": "estudiante@ejemplo.com",
  "isEvaluated": true
}
```

#### GET `/api/solutions/student/email/{email}`

Obtiene todas las soluciones enviadas por un estudiante específico.

**Response:**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "code": "function fibonacci(n) {...}",
    "feedback": "Excelente implementación...",
    "grade": 4,
    "submittedDate": "2025-07-09T14:30:00",
    "evaluatedDate": "2025-07-09T14:30:15",
    "exerciseId": "550e8400-e29b-41d4-a716-446655440001",
    "exerciseContent": "Implementa una función...",
    "studentName": "Juan Pérez",
    "studentEmail": "estudiante@ejemplo.com",
    "isEvaluated": true
  }
]
```

#### GET `/api/solutions/exercise/{exerciseId}`

Obtiene todas las soluciones enviadas para un ejercicio específico.

#### GET `/api/solutions/{solutionId}`

Obtiene los detalles de una solución específica.

#### GET `/api/solutions/unevaluated`

Obtiene todas las soluciones que aún no han sido evaluadas (útil para administradores).

#### GET `/api/solutions/latest/exercise/{exerciseId}/student/{email}`

Obtiene la última solución enviada por un estudiante para un ejercicio específico.

---

## Sistema de Evaluación Automática

### Proceso de Evaluación

1. **Envío de Código**: El estudiante envía su solución a través de `/api/solutions/submit`
2. **Almacenamiento**: La solución se guarda inmediatamente en la base de datos
3. **Evaluación con Gemini**: El código se envía a Gemini AI para evaluación automática
4. **Análisis**: Gemini analiza:
   - Corrección del código
   - Calidad y mejores prácticas
   - Solución del problema planteado
5. **Feedback y Nota**: Se recibe feedback detallado y una nota del 1 al 5
6. **Actualización**: La solución se actualiza con el feedback y la nota

### Criterios de Evaluación

- **Nota 1**: Muy deficiente (no funciona o está muy mal)
- **Nota 2**: Deficiente (funciona parcialmente con errores importantes)
- **Nota 3**: Regular (funciona pero con errores menores o puede mejorar)
- **Nota 4**: Bueno (funciona bien con algunas mejoras menores)
- **Nota 5**: Excelente (funciona perfectamente y está bien escrito)

---

### Ejemplos con curl:

```bash
# Enviar una solución
curl -X POST http://localhost:8080/api/solutions/submit \
  -H "Content-Type: application/json" \
  -d '{
    "exerciseId": "550e8400-e29b-41d4-a716-446655440001",
    "studentEmail": "estudiante@ejemplo.com",
    "code": "function fibonacci(n) { if (n <= 1) return n; return fibonacci(n-1) + fibonacci(n-2); }"
  }'

# Obtener soluciones de un estudiante
curl -X GET http://localhost:8080/api/solutions/student/email/estudiante@ejemplo.com

# Obtener la última solución de un estudiante para un ejercicio
curl -X GET http://localhost:8080/api/solutions/latest/exercise/550e8400-e29b-41d4-a716-446655440001/student/estudiante@ejemplo.com

# Obtener ejercicios de un estudiante
curl -X GET http://localhost:8080/api/exercises/student/550e8400-e29b-41d4-a716-446655440000

# Obtener ejercicios pendientes
curl -X GET http://localhost:8080/api/exercises/pending

# Marcar ejercicio como completado
curl -X PUT http://localhost:8080/api/exercises/550e8400-e29b-41d4-a716-446655440001/complete
```

---

## Comportamiento del Sistema

### Lógica de Sincronización

1. **UUID existente por ID**: Si existe un estudiante con el mismo UUID de Supabase, se actualizan sus datos.

2. **Email existente**: Si existe un estudiante con el mismo email pero diferente UUID, se actualiza el UUID con el de Supabase.

3. **Estudiante nuevo**: Si no existe ni por UUID ni por email, se crea un nuevo registro.

### Validaciones

- **UUID obligatorio**: Todos los endpoints requieren un UUID válido de Supabase
- **Email único**: Los emails deben ser únicos en la base de datos
- **Campos requeridos**: `id`, `email` y `fullName` son obligatorios en `/sync-from-supabase`
- **Relaciones**: Las soluciones deben estar asociadas a ejercicios y estudiantes válidos

---

## Códigos de Respuesta

| Código | Descripción |
|--------|-------------|
| `200 OK` | Operación exitosa |
| `400 Bad Request` | Datos inválidos o faltantes |
| `404 Not Found` | Recurso no encontrado |
| `500 Internal Server Error` | Error interno del servidor |

---

## Integración con Frontend

### Flujo Completo Recomendado

1. **Autenticación**: Usuario se autentica con Supabase
2. **Obtener datos**: Frontend obtiene `user.id`, `user.email` y `user.user_metadata.full_name` de Supabase
3. **Sincronización**: Enviar datos a `/api/student/sync-from-supabase`
4. **Chat**: Usar los mismos datos para `/api/chat/send`
5. **Consultar ejercicios**: Usar `/api/exercises/student/{studentId}` para ver historial
6. **Enviar solución**: Usar `/api/solutions/submit` cuando el estudiante termine su código
7. **Ver feedback**: Consultar la solución evaluada para ver el feedback y la nota
8. **Completar ejercicio**: Usar `/api/exercises/{exerciseId}/complete` cuando se considere finalizado

### Ejemplo de Integración JavaScript

```javascript
// Después de la autenticación con Supabase
const { data: { user } } = await supabase.auth.getUser();

const studentData = {
  id: user.id,
  email: user.email,
  fullName: user.user_metadata.full_name
};

// 1. Sincronizar con backend
await fetch('/api/student/sync-from-supabase', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(studentData)
});

// 2. Generar ejercicio (se guarda automáticamente)
const exerciseResponse = await fetch('/api/chat/send', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(studentData)
});

const exerciseContent = await exerciseResponse.text();

// 3. Obtener historial de ejercicios
const exercisesResponse = await fetch(`/api/exercises/student/${user.id}`);
const exercises = await exercisesResponse.json();

// 4. Enviar solución de código
const solutionData = {
  exerciseId: exercises[0].id, // ID del ejercicio actual
  studentEmail: user.email,
  code: `function fibonacci(n) {
    if (n <= 1) return n;
    return fibonacci(n-1) + fibonacci(n-2);
  }`
};

const solutionResponse = await fetch('/api/solutions/submit', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(solutionData)
});

const evaluatedSolution = await solutionResponse.json();
console.log('Feedback:', evaluatedSolution.feedback);
console.log('Nota:', evaluatedSolution.grade);

// 5. Obtener todas las soluciones del estudiante
const solutionsResponse = await fetch(`/api/solutions/student/email/${user.email}`);
const allSolutions = await solutionsResponse.json();

// 6. Marcar ejercicio como completado (cuando el usuario termine)
// await fetch(`/api/exercises/${exerciseId}/complete`, { method: 'PUT' });
```
