# API Documentation - ProyectoGrado

## Descripción General
API REST para un sistema de gestión de ejercicios de programación con evaluación automática de código Java. El sistema incluye integración con Gemini AI para generación de ejercicios y evaluación, y JDoodle para validación de compilación de código.

## Base URL
```
http://localhost:8080/api
```

## Configuración CORS
La API tiene CORS habilitado para permitir requests desde cualquier origen (`origins = "*"`).

---

## 🎓 Student Management

### POST /api/student/save
Guarda un estudiante en la base de datos.

**Request Body:**
```json
{
    "id": "a123b456-c789-0123-d456-e789f0123456",
    "fullName": "Juan Pérez",
    "email": "juan.perez@email.com"
}
```

**Response:**
```json
{
    "id": "a123b456-c789-0123-d456-e789f0123456",
    "fullName": "Juan Pérez",
    "email": "juan.perez@email.com",
    "version": 0
}
```

### POST /api/student/create
Crea un nuevo estudiante usando DTO.

**Request Body:**
```json
{
    "id": "a123b456-c789-0123-d456-e789f0123456",
    "fullName": "María García",
    "email": "maria.garcia@email.com"
}
```

**Response:**
```json
{
    "id": "a123b456-c789-0123-d456-e789f0123456",
    "fullName": "María García",
    "email": "maria.garcia@email.com",
    "version": 0
}
```

### PUT /api/student/update/{id}
Actualiza un estudiante existente.

**Path Parameters:**
- `id`: UUID del estudiante

**Request Body:**
```json
{
    "fullName": "María García Actualizada",
    "email": "maria.garcia.updated@email.com"
}
```

### POST /api/student/sync-from-supabase
Sincroniza un estudiante desde Supabase.

**Request Body:**
```json
{
    "id": "a123b456-c789-0123-d456-e789f0123456",
    "fullName": "Carlos Rodriguez",
    "email": "carlos.rodriguez@email.com"
}
```

---

## 🤖 Chat & Exercise Generation

### POST /api/chat/send
Envía datos del estudiante a Gemini AI para generar un ejercicio personalizado.

**Request Body:**
```json
{
    "id": "a123b456-c789-0123-d456-e789f0123456",
    "fullName": "Ana López",
    "email": "ana.lopez@email.com"
}
```

**Response:**
```json
{
    "exerciseId": "b234c567-d890-1234-e567-f890a1234567",
    "exerciseContent": "Crear una clase Calculator con métodos para suma, resta, multiplicación y división..."
}
```

---

## 📚 Exercise Management

### GET /api/exercises/student/{studentId}
Obtiene todos los ejercicios asignados a un estudiante por ID.

**Path Parameters:**
- `studentId`: UUID del estudiante

**Response:**
```json
[
    {
        "id": "b234c567-d890-1234-e567-f890a1234567",
        "exerciseContent": "Crear una clase Calculator...",
        "assignedDate": "2025-07-11T23:30:00",
        "isCompleted": false,
        "student": {
            "id": "a123b456-c789-0123-d456-e789f0123456",
            "fullName": "Ana López",
            "email": "ana.lopez@email.com"
        }
    }
]
```

### GET /api/exercises/student/email/{email}
Obtiene todos los ejercicios asignados a un estudiante por email.

**Path Parameters:**
- `email`: Email del estudiante

**Response:** *(Mismo formato que el endpoint anterior)*

### GET /api/exercises/pending
Obtiene todos los ejercicios pendientes (no completados).

**Response:**
```json
[
    {
        "id": "b234c567-d890-1234-e567-f890a1234567",
        "exerciseContent": "Crear una clase Calculator...",
        "assignedDate": "2025-07-11T23:30:00",
        "isCompleted": false,
        "student": {
            "id": "a123b456-c789-0123-d456-e789f0123456",
            "fullName": "Ana López",
            "email": "ana.lopez@email.com"
        }
    }
]
```

### PUT /api/exercises/{exerciseId}/complete
Marca un ejercicio como completado.

**Path Parameters:**
- `exerciseId`: UUID del ejercicio

**Response:**
```json
{
    "id": "b234c567-d890-1234-e567-f890a1234567",
    "exerciseContent": "Crear una clase Calculator...",
    "assignedDate": "2025-07-11T23:30:00",
    "isCompleted": true,
    "student": {
        "id": "a123b456-c789-0123-d456-e789f0123456",
        "fullName": "Ana López",
        "email": "ana.lopez@email.com"
    }
}
```

### GET /api/exercises/{exerciseId}
Obtiene un ejercicio específico por ID.

**Path Parameters:**
- `exerciseId`: UUID del ejercicio

### GET /api/exercises/{exerciseId}/solutions
Obtiene todas las soluciones de un ejercicio específico.

**Path Parameters:**
- `exerciseId`: UUID del ejercicio

### GET /api/exercises/{exerciseId}/solutions/latest/student/{email}
Obtiene la última solución de un estudiante para un ejercicio específico.

**Path Parameters:**
- `exerciseId`: UUID del ejercicio
- `email`: Email del estudiante

---

## 💻 Solution Management

### POST /api/solutions/submit
Envía una solución de código Java. **Incluye validación automática con JDoodle.**

**Request Body:**
```json
{
    "exerciseId": "b234c567-d890-1234-e567-f890a1234567",
    "studentEmail": "ana.lopez@email.com",
    "code": "public class Calculator {\n    public int sum(int a, int b) {\n        return a + b;\n    }\n}"
}
```

**Response (Éxito):**
```json
{
    "id": "c345d678-e901-2345-f678-901a2345b678",
    "code": "public class Calculator {\n    public int sum(int a, int b) {\n        return a + b;\n    }\n}",
    "feedback": "Excelente implementación...",
    "grade": 4.5,
    "submittedDate": "2025-07-11T23:45:00",
    "evaluatedDate": "2025-07-11T23:45:30",
    "exerciseId": "b234c567-d890-1234-e567-f890a1234567",
    "exerciseContent": "Crear una clase Calculator...",
    "studentName": "Ana López",
    "studentEmail": "ana.lopez@email.com",
    "isEvaluated": true
}
```

**Response (Error de Compilación - HTTP 400):**
```json
{
    "error": "COMPILATION_ERROR",
    "message": "El código no compila correctamente",
    "compilationError": "Main.java:3: error: ';' expected\n        int x = 5\n                 ^\n1 error",
    "jdoodleOutput": "Main.java:3: error: ';' expected\n        int x = 5\n                 ^\n1 error"
}
```

### GET /api/solutions/student/email/{email}
Obtiene todas las soluciones de un estudiante por email.

**Path Parameters:**
- `email`: Email del estudiante

### GET /api/solutions/exercise/{exerciseId}
Obtiene todas las soluciones de un ejercicio específico.

**Path Parameters:**
- `exerciseId`: UUID del ejercicio

### GET /api/solutions/{solutionId}
Obtiene una solución específica por ID.

**Path Parameters:**
- `solutionId`: UUID de la solución

### GET /api/solutions/unevaluated
Obtiene todas las soluciones no evaluadas.

**Response:**
```json
[
    {
        "id": "c345d678-e901-2345-f678-901a2345b678",
        "code": "public class Calculator { ... }",
        "feedback": null,
        "grade": null,
        "submittedDate": "2025-07-11T23:45:00",
        "evaluatedDate": null,
        "exerciseId": "b234c567-d890-1234-e567-f890a1234567",
        "exerciseContent": "Crear una clase Calculator...",
        "studentName": "Ana López",
        "studentEmail": "ana.lopez@email.com",
        "isEvaluated": false
    }
]
```

### GET /api/solutions/latest/exercise/{exerciseId}/student/{email}
Obtiene la última solución de un estudiante para un ejercicio específico.

**Path Parameters:**
- `exerciseId`: UUID del ejercicio
- `email`: Email del estudiante

---

## 🔧 JDoodle Integration (Code Validation)

### POST /api/jdoodle/validate
Valida y ejecuta código Java usando JDoodle.

**Request Body:**
```json
{
    "code": "public class Test {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}"
}
```

**Response:**
```json
{
    "output": "Hello, World!",
    "statusCode": 200,
    "memory": "13556",
    "cpuTime": "0.09",
    "compilationStatus": null,
    "isCompiled": true,
    "error": null,
    "projectKey": "abc123def456"
}
```

### POST /api/jdoodle/compile-only
Solo compila código Java sin ejecutarlo (más rápido).

**Request Body:**
```json
{
    "code": "public class Calculator {\n    public int sum(int a, int b) {\n        return a + b;\n    }\n}"
}
```

**Response:**
```json
{
    "output": "",
    "statusCode": 200,
    "memory": "0",
    "cpuTime": "0.05",
    "compilationStatus": "compiled successfully",
    "isCompiled": true,
    "error": null,
    "projectKey": "def456ghi789"
}
```

### GET /api/jdoodle/test
Prueba la conectividad con JDoodle usando código de ejemplo.

**Response:**
```json
{
    "status": "success",
    "message": "Conexión con JDoodle exitosa",
    "isCompiled": true,
    "output": "Hello, World!",
    "statusCode": 200,
    "cpuTime": "0.09",
    "memory": "13556",
    "compilationStatus": null,
    "projectKey": "abc123def456"
}
```

---

## 📊 Data Models

### Student
```json
{
    "id": "UUID - Generated by Supabase",
    "fullName": "string",
    "email": "string (unique)",
    "version": "long - Optimistic locking"
}
```

### Exercise
```json
{
    "id": "UUID - Auto-generated",
    "exerciseContent": "string - Exercise description/requirements",
    "assignedDate": "LocalDateTime - Auto-set on creation",
    "isCompleted": "boolean - Default: false",
    "student": "Student object"
}
```

### Solution
```json
{
    "id": "UUID - Auto-generated",
    "code": "string - Java code submitted",
    "feedback": "string - AI evaluation feedback",
    "grade": "double - Grade from 0 to 5",
    "submittedDate": "LocalDateTime - Auto-set on creation",
    "evaluatedDate": "LocalDateTime - Set when evaluated",
    "exercise": "Exercise object",
    "student": "Student object",
    "isEvaluated": "boolean - Default: false"
}
```

---

## 🚨 Error Handling

### Global Exception Handler
La API maneja automáticamente varios tipos de errores:

#### Compilation Errors (HTTP 400)
```json
{
    "error": "COMPILATION_ERROR",
    "message": "El código no compila correctamente",
    "compilationError": "Error details from JDoodle",
    "jdoodleOutput": "Full compilation output"
}
```

#### Optimistic Locking Errors (HTTP 409)
```json
{
    "error": "CONCURRENT_MODIFICATION",
    "message": "Los datos fueron modificados por otra transacción. Por favor, actualice los datos e intente nuevamente.",
    "details": "Technical error details"
}
```

#### General Server Errors (HTTP 500)
```json
{
    "error": "INTERNAL_SERVER_ERROR",
    "message": "Ha ocurrido un error interno del servidor.",
    "details": "Technical error details"
}
```

#### Submission Errors (HTTP 400)
```json
{
    "error": "SUBMISSION_ERROR",
    "message": "Specific error message"
}
```

---

## 🔄 Workflow Integration

### Complete Learning Flow
1. **Student Registration**: `POST /api/student/create`
2. **Exercise Generation**: `POST /api/chat/send`
3. **Code Submission**: `POST /api/solutions/submit`
   - ✅ **Automatic JDoodle validation**
   - ✅ **Automatic Gemini AI evaluation**
4. **Progress Tracking**: `GET /api/exercises/student/email/{email}`

### AI Integration Features
- **Gemini AI**: Exercise generation and code evaluation
- **JDoodle**: Real-time code compilation validation
- **Automatic Feedback**: Grade and detailed feedback generation

### Security & Performance
- **Optimistic Locking**: Prevents concurrent modification conflicts
- **CORS Enabled**: Cross-origin requests supported
- **Input Validation**: Comprehensive request validation
- **Error Handling**: Detailed error responses for debugging

---

## 📝 Notes

- All UUIDs are generated automatically except for Student ID (comes from Supabase)
- Timestamps are managed automatically by JPA lifecycle hooks
- Code validation with JDoodle happens before database storage
- AI evaluation with Gemini happens after successful code validation
- The system uses compile-only validation for efficiency in solution submission
