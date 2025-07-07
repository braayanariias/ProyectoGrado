# API Documentation

## Endpoint para generar ejercicio de programación

### POST `/api/chat/send`

Este endpoint recibe un estudiante con su información (id, email, fullName), lo guarda en la base de datos y luego genera un ejercicio de programación utilizando el modelo de IA.

#### Request Body

```json
{
  "id": null,
  "email": "estudiante@ejemplo.com",
  "fullName": "Juan Pérez"
}
```

**Nota:** El campo `id` debe ser `null` para estudiantes nuevos, ya que se genera automáticamente por la base de datos.

#### Response

```json
"Ejercicio de programación generado por la IA..."
```

#### Ejemplo de uso con curl

```bash
curl -X POST http://localhost:8080/api/chat/send \
  -H "Content-Type: application/json" \
  -d '{
    "id": null,
    "email": "estudiante@ejemplo.com",
    "fullName": "Juan Pérez"
  }'
```

#### Comportamiento

1. **Si el estudiante no existe**: Se crea un nuevo registro en la base de datos con la información proporcionada.
2. **Si el estudiante ya existe** (mismo email): Se actualiza el `fullName` del estudiante existente.
3. **Después de guardar**: Se procede a generar el ejercicio de programación utilizando el modelo de IA.

#### Códigos de respuesta

- `200 OK`: Ejercicio generado exitosamente
- `400 Bad Request`: Datos del estudiante inválidos
- `500 Internal Server Error`: Error interno del servidor o del modelo de IA
