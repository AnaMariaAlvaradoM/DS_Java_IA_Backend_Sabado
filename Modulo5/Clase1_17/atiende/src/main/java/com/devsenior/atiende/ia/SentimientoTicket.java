package com.devsenior.atiende.ia;

import java.util.Locale;

public enum SentimientoTicket {
    MOLESTO,
    NEUTRAL,
    CONTENTO;

    public static SentimientoTicket desdeTexto(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new RespuestaInvalidaIAException("La IA no devolvió un sentimiento válido.");
        }

        String normalizado = valor.trim().toUpperCase(Locale.ROOT).replaceAll("[^A-Z_]", "");
        for (SentimientoTicket sentimiento : values()) {
            if (sentimiento.name().equals(normalizado)) {
                return sentimiento;
            }
        }

        throw new RespuestaInvalidaIAException("La respuesta de la IA no es un sentimiento válido: " + valor);
    }
}
