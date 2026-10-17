package com.devsenior.atiende.ia;

public class IaNoDisponibleException extends RuntimeException {

    public IaNoDisponibleException() {
        super("Ningún proveedor de IA está disponible en este momento");
    }
}
