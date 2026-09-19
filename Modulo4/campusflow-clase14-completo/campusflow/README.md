# CampusFlow

Proyecto de referencia de CampusFlow tal como debió quedar al cierre de la **Clase 14** del Módulo 4 (Java AI Engineer, Dev Senior Code): arquitectura modular por dominios, relaciones reales entre `Usuario` y `Curso`, DTOs con mapeo explícito, y login/autenticación real con JWT (reutilizando el patrón ya visto en la Clase 11).

Este código corresponde exactamente a las versiones "final consolidada" de la guía — sin comentarios de `// getters y setters`, con los métodos ya escritos, listo para correr.

## Stack

Java 17 · Spring Boot 3.3.4 · Maven · MySQL · Spring Security + JWT (jjwt 0.13.0)

## Cómo correrlo

1. Crea la base de datos en MySQL:

   ```sql
   CREATE DATABASE campusflow_db;
   ```

2. Define la variable de entorno `SPRING_PROFILES_ACTIVE=dev` antes de correr el proyecto (en Cursor/VS Code: `Run and Debug` → configuración de arranque → `Environment variables`; en línea de comandos: `SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run`).

3. Si tu MySQL local no usa usuario `root` / password `root`, exporta `DB_PASSWORD` con tu contraseña real, o edita `application-dev.properties`.

4. Arranca el proyecto. Hibernate crea las tablas solo (`ddl-auto=update` en `dev`).

## El primer administrador

El registro público (`POST /api/auth/register`) siempre crea usuarios con rol `ESTUDIANTE` — nadie puede autoasignarse un rol mayor. El primer `ADMINISTRADOR` se promueve a mano en la base de datos: ver `scripts/primer-admin.sql`. Después de promoverlo hay que volver a hacer login para obtener un token con el rol actualizado (el rol viaja dentro del JWT, no se vuelve a consultar en cada petición).

## Endpoints principales

**Públicos:** `POST /api/auth/register`, `POST /api/auth/login`, `GET /api/cursos`.

**Solo `INSTRUCTOR` o `ADMINISTRADOR`:** `POST /api/cursos`.

**Solo `ADMINISTRADOR`:** todo `/api/usuarios/**` (`GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}`).

**Cualquier usuario autenticado:** el resto, incluida la inscripción `POST /api/cursos/{cursoId}/estudiantes/{estudianteId}`.

Las peticiones protegidas necesitan la cabecera `Authorization: Bearer <token>`.

## Estructura del proyecto

```text
com.devsenior.campusflow
 ├─ common/exception       (ResourceNotFoundException, EmailDuplicadoException, GlobalExceptionHandler)
 ├─ security               (JwtService, UsuarioDetailsService, JwtAuthFilter, SecurityConfig)
 ├─ usuarios
 │   ├─ model               (Usuario, RolUsuario, PreferenciasUsuario, TemaVisual)
 │   ├─ dto                 (CrearUsuarioRequest, ActualizarUsuarioRequest, UsuarioResponse,
 │   │                        RegistroRequest, LoginRequest, AuthResponse)
 │   ├─ mapper               (UsuarioMapper)
 │   ├─ repository           (UsuarioRepository)
 │   ├─ service              (UsuarioService, AuthService)
 │   └─ controller           (UsuarioController, AuthController)
 └─ cursos
     ├─ model               (Curso)
     ├─ dto                  (CrearCursoRequest, CursoResponse)
     ├─ mapper               (CursoMapper)
     ├─ repository           (CursoRepository)
     ├─ service              (CursoService)
     └─ controller           (CursoController)
```

Este proyecto es material de apoyo para la instructora — la guía completa, con explicación paso a paso, está en `Módulo 4 - Clase 14 - Usuarios, Perfiles y Relaciones Reales.md`.
