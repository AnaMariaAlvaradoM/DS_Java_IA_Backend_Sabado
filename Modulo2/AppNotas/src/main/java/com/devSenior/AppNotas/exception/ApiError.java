package com.devSenior.AppNotas.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApiError {

    private LocalDateTime timestamp;
    private int estado;
    private String error;
    private String mensaje;
    private String ruta;
    private List<String> detalles = new ArrayList<>();

    public ApiError(int estado, String error, String mensaje, String ruta) {
        this.timestamp = LocalDateTime.now();
        this.estado = estado;
        this.error = error;
        this.mensaje = mensaje;
        this.ruta = ruta;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getEstado() { return estado; }
    public String getError() { return error; }
    public String getMensaje() { return mensaje; }
    public String getRuta() { return ruta; }
    public List<String> getDetalles() { return detalles; }
    public void setDetalles(List<String> detalles) { this.detalles = detalles; }
}