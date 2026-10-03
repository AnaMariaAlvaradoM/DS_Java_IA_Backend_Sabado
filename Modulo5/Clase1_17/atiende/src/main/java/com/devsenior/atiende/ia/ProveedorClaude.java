package com.devsenior.atiende.ia;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
@Order(2)
public class ProveedorClaude implements ProveedorIA {

    private static final Logger logger = LoggerFactory.getLogger(ProveedorClaude.class);

    private final IaProperties iaProperties;
    private final RestClient restClient;

    public ProveedorClaude(IaProperties iaProperties, RestClient.Builder restClientBuilder) {
        this.iaProperties = iaProperties;
        this.restClient = restClientBuilder.build();
    }

    @Override
    public String nombre() {
        return "claude";
    }

    @Override
    public RespuestaIA completar(String instrucciones, String mensaje) {
        long inicio = System.currentTimeMillis();
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("model", iaProperties.getClaude().getModelo());
        cuerpo.put("max_tokens", iaProperties.getMaxTokens());
        cuerpo.put("thinking", Map.of("type", "disabled"));
        cuerpo.put("system", instrucciones == null ? "" : instrucciones);
        cuerpo.put("messages", List.of(Map.of("role", "user", "content", mensaje == null ? "" : mensaje)));

        try {
            Map<String, Object> respuesta = restClient.post()
                    .uri(iaProperties.getClaude().getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + iaProperties.getClaude().getLlave())
                    .header("x-api-key", iaProperties.getClaude().getLlave())
                    .header("anthropic-version", "2023-06-01")
                    .body(cuerpo)
                    .retrieve()
                    .body(Map.class);

            String texto = extraerTexto(respuesta);
            String modelo = respuesta != null && respuesta.get("model") != null
                    ? respuesta.get("model").toString()
                    : iaProperties.getClaude().getModelo();
            Map<String, Object> uso = obtenerMapa(respuesta, "usage");
            long tokensEntrada = obtenerLong(uso, "input_tokens");
            long tokensSalida = obtenerLong(uso, "output_tokens");
            BigDecimal costoEstimado = calcularCosto(tokensEntrada, tokensSalida);

            return new RespuestaIA(
                    texto,
                    nombre(),
                    modelo,
                    tokensEntrada,
                    tokensSalida,
                    costoEstimado,
                    System.currentTimeMillis() - inicio);
        } catch (RestClientResponseException ex) {
            registrarError(ex.getStatusCode().value());
            throw new ServicioIAIndisponibleException("El proveedor claude no está disponible.");
        } catch (ResourceAccessException ex) {
            logger.warn("Proveedor claude no responde o el tiempo de espera expiró.");
            throw new ServicioIAIndisponibleException("El proveedor claude no está disponible.");
        }
    }

    private String extraerTexto(Map<String, Object> respuesta) {
        if (respuesta == null || respuesta.isEmpty()) {
            throw new RespuestaInvalidaIAException("La respuesta del proveedor claude está vacía.");
        }

        Object contenido = respuesta.get("content");
        if (!(contenido instanceof List<?> bloques) || bloques.isEmpty()) {
            throw new RespuestaInvalidaIAException("La respuesta del proveedor claude no contiene bloques de texto.");
        }

        StringBuilder texto = new StringBuilder();
        for (Object bloque : bloques) {
            if (bloque instanceof Map<?, ?> mapa) {
                Object tipo = mapa.get("type");
                if ("text".equals(tipo)) {
                    Object valor = mapa.get("text");
                    if (valor != null) {
                        texto.append(valor);
                    }
                }
            }
        }

        if (texto.length() == 0) {
            throw new RespuestaInvalidaIAException("La respuesta del proveedor claude no incluye texto válido.");
        }

        return texto.toString();
    }

    private BigDecimal calcularCosto(long tokensEntrada, long tokensSalida) {
        BigDecimal precioEntrada = iaProperties.getClaude().getPrecioEntradaMillon();
        BigDecimal precioSalida = iaProperties.getClaude().getPrecioSalidaMillon();

        BigDecimal costoEntrada = BigDecimal.valueOf(tokensEntrada)
                .multiply(precioEntrada)
                .divide(BigDecimal.valueOf(1_000_000L), 12, BigDecimal.ROUND_HALF_UP);
        BigDecimal costoSalida = BigDecimal.valueOf(tokensSalida)
                .multiply(precioSalida)
                .divide(BigDecimal.valueOf(1_000_000L), 12, BigDecimal.ROUND_HALF_UP);
        return costoEntrada.add(costoSalida);
    }

    private Map<String, Object> obtenerMapa(Map<String, Object> respuesta, String clave) {
        Object valor = respuesta == null ? null : respuesta.get(clave);
        if (valor instanceof Map<?, ?> mapa) {
            @SuppressWarnings("unchecked")
            Map<String, Object> convertido = (Map<String, Object>) mapa;
            return convertido;
        }
        return Map.of();
    }

    private long obtenerLong(Map<String, Object> mapa, String clave) {
        Object valor = mapa == null ? null : mapa.get(clave);
        if (valor instanceof Number numero) {
            return numero.longValue();
        }
        return 0L;
    }

    private void registrarError(int codigoHttp) {
        if (codigoHttp == 400 || codigoHttp == 401 || codigoHttp == 403 || codigoHttp == 404) {
            logger.error("Configuración inválida para el proveedor claude: HTTP {}.", codigoHttp);
        } else {
            logger.warn("El proveedor claude falló con HTTP {}.", codigoHttp);
        }
    }
}
