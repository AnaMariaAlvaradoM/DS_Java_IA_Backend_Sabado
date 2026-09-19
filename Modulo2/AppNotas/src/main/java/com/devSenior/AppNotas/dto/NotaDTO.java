package com.devSenior.AppNotas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

//! Datos que envia el cliente para editar o crear una nota
public class NotaDTO {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 100, message = "No debe superar los 100 caracteres")
    private String titulo;

    @NotBlank(message = "El titulo es obligatorio")
    private String contenido;

    private List<String> etiquetas = new ArrayList<>();

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }
}

