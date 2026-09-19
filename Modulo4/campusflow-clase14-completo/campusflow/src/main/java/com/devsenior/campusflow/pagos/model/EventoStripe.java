package com.devsenior.campusflow.pagos.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "eventos_stripe")
public class EventoStripe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String stripeEventId;

    private String tipo;
    private LocalDateTime recibidoEn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStripeEventId() {
        return stripeEventId;
    }

    public void setStripeEventId(String stripeEventId) {
        this.stripeEventId = stripeEventId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getRecibidoEn() {
        return recibidoEn;
    }

    public void setRecibidoEn(LocalDateTime recibidoEn) {
        this.recibidoEn = recibidoEn;
    }
}