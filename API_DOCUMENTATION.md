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

## Comportamiento del Sistema

### Lógica de Sincronización

1. **UUID existente por ID**: Si existe un estudiante con el mismo UUID de Supabase, se actualizan sus datos.

2. **Email existente**: Si existe un estudiante con el mismo email pero diferente UUID, se actualiza el UUID con el de Supabase.

3. **Estudiante nuevo**: Si no existe ni por UUID ni por email, se crea un nuevo registro.

### Validaciones

- **UUID obligatorio**: Todos los endpoints requieren un UUID válido de Supabase
- **Email único**: Los emails deben ser únicos en la base de datos
- **Campos requeridos**: `id`, `email` y `fullName` son obligatorios en `/sync-from-supabase`

---

## Códigos de Respuesta

| Código | Descripción |
|--------|-------------|
| `200 OK` | Operación exitosa |
| `400 Bad Request` | Datos inválidos o faltantes |
| `500 Internal Server Error` | Error interno del servidor |

---

## Integración con Frontend

### Flujo Recomendado

1. **Autenticación**: Usuario se autentica con Supabase
2. **Obtener datos**: Frontend obtiene `user.id`, `user.email` y `user.user_metadata.full_name` de Supabase
3. **Sincronización**: Enviar datos a `/api/student/sync-from-supabase`
4. **Chat**: Usar los mismos datos para `/api/chat/send`

### Ejemplo de Integración JavaScript

```javascript
// Después de la autenticación con Supabase
const { data: { user } } = await supabase.auth.getUser();

const studentData = {
  id: user.id,
  email: user.email,
  fullName: user.user_metadata.full_name
};

// Sincronizar con backend
await fetch('/api/student/sync-from-supabase', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(studentData)
});

// Generar ejercicio
const response = await fetch('/api/chat/send', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(studentData)
});
```
