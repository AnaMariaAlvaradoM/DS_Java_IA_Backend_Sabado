package com.devSenior.AppNotas.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Document(collection = "notas")
public class Nota {

    @Id
    private String id;

    private String titulo;
    private String contenido;

    // EMBEBIDO: las etiquetas viven dentro de la nota
    private List<String> etiquetas = new ArrayList<>();

    // REFERENCIA: solo guardamos el id del usuario dueño
    private String usuarioId;

    private LocalDateTime fechaCreacion;

    public Nota() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public List<String> getEtiquetas() { return etiquetas; }
    public void setEtiquetas(List<String> etiquetas) { this.etiquetas = etiquetas; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}

