package com.devsenior.atiende.ia;

import java.math.BigDecimal;

public record RespuestaIA(
        String texto,
        String proveedor,
        String modelo,
        long tokensEntrada,
        long tokensSalida,
        BigDecimal costoEstimado,
        long milisegundos) {
}
