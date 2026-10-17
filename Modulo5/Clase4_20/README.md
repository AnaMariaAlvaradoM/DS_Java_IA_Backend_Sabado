# Atiende

Backend de atención al cliente de **Grano Alto Café**.
Centraliza en tickets los mensajes que llegan por WhatsApp, correo y chat web.

## Requisitos

- Java 17
- Maven (o el wrapper `mvnw` incluido)
- MySQL 8 corriendo en `localhost:3306`

## Cómo ejecutar

1. Revisa usuario y contraseña de MySQL en `src/main/resources/application.properties`.
2. Desde la raíz del proyecto:

```bash
./mvnw spring-boot:run
```

La base `atiende_db` se crea sola la primera vez.

## Configurar la IA (Clase 18)

1. Copia `secrets.example.properties` como `secrets.properties` (en la raíz) y pon tu llave de Gemini.
2. Opcional: instala Ollama y ejecuta `ollama pull qwen3:1.7b` para tener el proveedor de respaldo.
3. Ejecuta la aplicación **desde la raíz del proyecto**, para que encuentre `secrets.properties`.

Sin llave ni Ollama la aplicación arranca igual; el análisis de sentimiento responderá 503.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/tickets` | Registra un mensaje entrante como ticket |
| GET | `/api/tickets` | Lista tickets (filtro opcional `?estado=`) |
| GET | `/api/tickets/{id}` | Consulta un ticket |
| PATCH | `/api/tickets/{id}/estado` | Cambia el estado de un ticket |
| POST | `/api/tickets/{id}/sentimiento` | Analiza con IA si el cliente está MOLESTO, NEUTRAL o CONTENTO |

### Crear un ticket

```bash
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{"clienteNombre":"Laura Gómez","clienteContacto":"+57 300 123 4567","canal":"WHATSAPP","mensaje":"Pedí el pedido 1045 hace 8 días y no llega"}'
```

Respuesta `201 Created` con `estado: ABIERTO` y `prioridad: MEDIA`.

### Cambiar estado

```bash
curl -X PATCH http://localhost:8080/api/tickets/1/estado \
  -H "Content-Type: application/json" \
  -d '{"estado":"EN_PROCESO"}'
```

Transiciones permitidas:

| Desde | Hacia |
|---|---|
| ABIERTO | EN_PROCESO, CERRADO |
| EN_PROCESO | RESUELTO |
| RESUELTO | CERRADO, EN_PROCESO |
| CERRADO | — (estado final) |

Una transición no permitida responde `409 Conflict`.

## Errores

Todas las respuestas de error tienen la forma:

```json
{ "codigo": 404, "error": "Not Found", "mensaje": "No existe el ticket con id 99", "fecha": "..." }
```

## Tests

```bash
./mvnw test
```

`AtiendeApplicationTests` necesita MySQL encendido; los demás tests son unitarios y no llaman a ninguna API de IA (38 tests en total).
