package com.devsenior.campusflow.usuarios.dto;

import com.devsenior.campusflow.usuarios.model.RolUsuario;
import com.devsenior.campusflow.usuarios.model.TemaVisual;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String email;
    private RolUsuario rol;
    private TemaVisual tema;
    private boolean notificacionesActivas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public TemaVisual getTema() {
        return tema;
    }

    public void setTema(TemaVisual tema) {
        this.tema = tema;
    }

    public boolean isNotificacionesActivas() {
        return notificacionesActivas;
    }

    public void setNotificacionesActivas(boolean notificacionesActivas) {
        this.notificacionesActivas = notificacionesActivas;
    }
}
