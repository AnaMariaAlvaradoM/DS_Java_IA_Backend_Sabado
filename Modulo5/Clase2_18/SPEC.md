# SPEC · Atiende · Fase 1: recepción de tickets

## Problema
Grano Alto Café (tienda online de café de especialidad) recibe mensajes por WhatsApp, correo y chat web.
Dos personas los leen a mano en tres lugares distintos: lo urgente se pierde y nadie sabe cuántos mensajes siguen sin respuesta.

## Objetivo de la fase 1
Centralizar todos los mensajes como tickets con estado, para saber qué falta por atender.

## Fuera de alcance (llega después)
- IA: clasificación, prioridad automática y respuestas sugeridas.
- Pedidos de la tienda.
- Autenticación.
- Conexión real con WhatsApp o correo: el canal se simula con peticiones HTTP.

## Entidad Ticket

| Campo | Tipo | Regla |
|---|---|---|
| id | Long | Autogenerado |
| clienteNombre | Texto | Obligatorio, máximo 100 |
| clienteContacto | Texto | Obligatorio (teléfono o correo), máximo 120 |
| canal | WHATSAPP, CORREO, CHAT_WEB | Obligatorio |
| mensaje | Texto | Obligatorio, máximo 2000 |
| estado | ABIERTO, EN_PROCESO, RESUELTO, CERRADO | Lo asigna el sistema |
| prioridad | BAJA, MEDIA, ALTA, URGENTE | Lo asigna el sistema |
| fechaCreacion | Fecha y hora | Automática |
| fechaActualizacion | Fecha y hora | Automática, cambia en cada modificación |

## Reglas de negocio
1. Todo ticket nuevo nace ABIERTO y con prioridad MEDIA. El cliente no puede enviar estado ni prioridad.
2. Transiciones de estado permitidas:

| Desde | Hacia | Caso real |
|---|---|---|
| ABIERTO | EN_PROCESO | Alguien de soporte toma el ticket |
| ABIERTO | CERRADO | Spam o mensaje duplicado |
| EN_PROCESO | RESUELTO | Se respondió al cliente |
| RESUELTO | CERRADO | El cliente confirmó la solución |
| RESUELTO | EN_PROCESO | El cliente dice que no quedó resuelto |

   Cualquier otra transición se rechaza. CERRADO es un estado final.
3. La regla de transiciones vive en el enum `EstadoTicket`, en el método `boolean puedeCambiarA(EstadoTicket destino)`.

## Endpoints

| Método | Ruta | Respuestas |
|---|---|---|
| POST | /api/tickets | 201 ticket creado · 400 datos inválidos |
| GET | /api/tickets?estado= | 200 lista, más recientes primero; el filtro es opcional |
| GET | /api/tickets/{id} | 200 · 404 |
| PATCH | /api/tickets/{id}/estado | 200 · 400 · 404 · 409 transición no permitida |

## Formato de error
JSON con: `codigo`, `error`, `mensaje`, `fecha`.

## Criterios de aceptación
- [ ] Crear un ticket devuelve 201 con estado ABIERTO y prioridad MEDIA.
- [ ] Si el cliente envía estado o prioridad, se ignoran.
- [ ] Enviar el mensaje vacío devuelve 400 indicando el campo.
- [ ] Buscar un id inexistente devuelve 404.
- [ ] ABIERTO → EN_PROCESO devuelve 200 y la respuesta trae la fechaActualizacion nueva.
- [ ] ABIERTO → RESUELTO devuelve 409.
- [ ] Filtrar con estado=ABIERTO solo devuelve tickets abiertos.

---

# SPEC · Atiende · Fase 2: la IA entra al producto

## Problema
El equipo de soporte no sabe qué clientes están molestos hasta leer cada mensaje.
Antes de automatizar más (Fase 3), necesitamos que Atiende pueda hablar con un modelo de IA de forma confiable, segura y con costo conocido.

## Objetivo de la fase 2
Detectar el sentimiento de un ticket (MOLESTO, NEUTRAL, CONTENTO) usando IA, con respaldo automático si el proveedor principal falla.

## Fuera de alcance (llega después)
- Guardar el sentimiento en la base de datos.
- Clasificación por categoría, prioridad automática, borradores de respuesta y chatbot.
- Autenticación (deuda técnica declarada: este endpoint consume créditos).

## Proveedores
| Orden | Nombre | Formato de API | Servicio en clase | Servicio en producción |
|---|---|---|---|---|
| 1 | openai | Chat Completions (OpenAI) | Gemini, plan gratuito | OpenAI |
| 2 | claude | Messages (Anthropic) | Ollama local | Claude |

## Reglas
1. Si un proveedor falla, se intenta el siguiente. Si todos fallan: 503.
2. Errores HTTP 400, 401, 403 y 404 son de configuración: se registran como ERROR. Los demás (429, 5xx, sin conexión, tiempo agotado, respuesta vacía) son temporales: WARN.
3. La respuesta del modelo se valida: si no es uno de los tres sentimientos, se responde 502.
4. Las llaves viven solo en `secrets.properties` (raíz del proyecto), nunca en el código ni en Git.
5. Cada análisis informa proveedor, modelo, tokens de entrada y salida, costo estimado en USD y milisegundos.
6. En los logs nunca se escribe el mensaje del cliente ni una llave.
7. Límites: tiempo máximo de espera y tokens máximos de salida, configurables.

## Endpoint
| Método | Ruta | Respuestas |
|---|---|---|
| POST | /api/tickets/{id}/sentimiento | 200 análisis · 404 ticket no existe · 502 respuesta inválida de la IA · 503 IA no disponible |

## Criterios de aceptación
- [ ] Con Gemini configurado, el análisis del ticket de Laura devuelve MOLESTO y `proveedor: openai`.
- [ ] Con una llave inválida, el análisis sigue funcionando con `proveedor: claude` y el log muestra un ERROR.
- [ ] Con Ollama apagado y llave inválida, la respuesta es 503.
- [ ] Una respuesta del modelo fuera de los tres valores produce 502.
- [ ] `secrets.properties` no aparece en `git status`.
