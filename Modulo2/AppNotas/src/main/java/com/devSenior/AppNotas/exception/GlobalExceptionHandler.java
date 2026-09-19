package com.devSenior.AppNotas.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatearError)
                .toList();

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                "Uno o más campos tienen errores",
                request.getRequestURI());
        apiError.setDetalles(detalles);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    private String formatearError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }

    // Recurso no encontrado → 404
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarNoEncontrado(
            RecursoNoEncontradoException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    // Acceso denegado → 403 (ownership de la Clase 4)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> manejarAccesoDenegado(
            AccessDeniedException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN.value(),
                "Acceso denegado", ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    // Credenciales inválidas en el login → 401
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> manejarCredenciales(
            BadCredentialsException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(),
                "Credenciales inválidas", "Usuario o contraseña incorrectos",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    // Red de seguridad → 500 SIN exponer el stack trace
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarGenerico(
            Exception ex, HttpServletRequest request) {
        ex.printStackTrace();  // el detalle va al log, para nosotros
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno", "Ocurrió un error inesperado. Intenta de nuevo más tarde.",
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
