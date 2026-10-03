package com.devsenior.atiende.ia;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "atiende.ia")
public class IaProperties {

    private Integer timeoutSegundos = 30;
    private Integer maxTokens = 1024;
    private ProveedorIaProperties openai = new ProveedorIaProperties();
    private ProveedorIaProperties claude = new ProveedorIaProperties();

    public Integer getTimeoutSegundos() {
        return timeoutSegundos;
    }

    public void setTimeoutSegundos(Integer timeoutSegundos) {
        this.timeoutSegundos = timeoutSegundos;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public ProveedorIaProperties getOpenai() {
        return openai;
    }

    public void setOpenai(ProveedorIaProperties openai) {
        this.openai = openai;
    }

    public ProveedorIaProperties getClaude() {
        return claude;
    }

    public void setClaude(ProveedorIaProperties claude) {
        this.claude = claude;
    }

    public static class ProveedorIaProperties {
        private String url;
        private String modelo;
        private String llave;
        private BigDecimal precioEntradaMillon = BigDecimal.ZERO;
        private BigDecimal precioSalidaMillon = BigDecimal.ZERO;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getModelo() {
            return modelo;
        }

        public void setModelo(String modelo) {
            this.modelo = modelo;
        }

        public String getLlave() {
            return llave;
        }

        public void setLlave(String llave) {
            this.llave = llave;
        }

        public BigDecimal getPrecioEntradaMillon() {
            return precioEntradaMillon;
        }

        public void setPrecioEntradaMillon(BigDecimal precioEntradaMillon) {
            this.precioEntradaMillon = precioEntradaMillon;
        }

        public BigDecimal getPrecioSalidaMillon() {
            return precioSalidaMillon;
        }

        public void setPrecioSalidaMillon(BigDecimal precioSalidaMillon) {
            this.precioSalidaMillon = precioSalidaMillon;
        }
    }
}
