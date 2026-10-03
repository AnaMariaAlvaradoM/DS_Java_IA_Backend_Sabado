package com.devsenior.atiende.ticket;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum Sentimiento {
    MOLESTO,
    NEUTRAL,
    CONTENTO;

    public static Optional<Sentimiento> desdeTextoIA(String texto) {
        if (texto == null) {
            return Optional.empty();
        }
        String limpio = texto.toUpperCase(Locale.ROOT).replaceAll("[^A-Z]", "");
        return Arrays.stream(values())
                .filter(sentimiento -> sentimiento.name().equals(limpio))
                .findFirst();
    }
}
