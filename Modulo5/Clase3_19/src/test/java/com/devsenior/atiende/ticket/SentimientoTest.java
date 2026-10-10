package com.devsenior.atiende.ticket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class SentimientoTest {

    @ParameterizedTest(name = "\"{0}\" se reconoce como {1}")
    @CsvSource({
            "MOLESTO, MOLESTO",
            "molesto, MOLESTO",
            "' Neutral.', NEUTRAL",
            "**CONTENTO**, CONTENTO"
    })
    void desdeTextoIA_textoValido(String texto, Sentimiento esperado) {
        assertEquals(Optional.of(esperado), Sentimiento.desdeTextoIA(texto));
    }

    @ParameterizedTest(name = "\"{0}\" se rechaza")
    @ValueSource(strings = {"CONTENO", "MOLEYSTO", "NEGATIVO", "El cliente está molesto", ""})
    void desdeTextoIA_textoInvalido(String texto) {
        assertTrue(Sentimiento.desdeTextoIA(texto).isEmpty());
    }
}
