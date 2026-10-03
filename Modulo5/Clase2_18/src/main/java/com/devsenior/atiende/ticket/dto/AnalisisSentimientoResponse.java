package com.devsenior.atiende.ticket.dto;

import com.devsenior.atiende.ticket.Sentimiento;
import java.math.BigDecimal;

public record AnalisisSentimientoResponse(
        Long ticketId,
        Sentimiento sentimiento,
        String proveedor,
        String modelo,
        int tokensEntrada,
        int tokensSalida,
        BigDecimal costoEstimadoUsd,
        long milisegundos
) {
}
