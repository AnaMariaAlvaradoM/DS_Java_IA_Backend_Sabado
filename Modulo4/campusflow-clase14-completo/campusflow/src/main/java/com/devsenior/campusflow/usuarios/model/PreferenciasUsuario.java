package com.devsenior.campusflow.usuarios.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class PreferenciasUsuario {

    @Enumerated(EnumType.STRING)
    private TemaVisual tema = TemaVisual.CLARO;

    private boolean notificacionesActivas = true;

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
