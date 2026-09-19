package com.devsenior.campusflow.pagos.service;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.devsenior.campusflow.common.exception.PlanNoDisponibleException;
import com.devsenior.campusflow.pagos.model.PlanSuscripcion;
import com.devsenior.campusflow.usuarios.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    private final StripeClient stripeClient;

    @Value("${stripe.price-pago-unico}")
    private String pricePagoUnico;

    @Value("${stripe.price-plan-individual}")
    private String pricePlanIndividual;

    @Value("${stripe.url-exito}")
    private String urlExito;

    @Value("${stripe.url-cancelado}")
    private String urlCancelado;

    public StripeService(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    public String crearCheckoutPagoUnico(Usuario usuario)
            throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(urlExito)
                .setCancelUrl(urlCancelado)
                .setCustomerEmail(usuario.getEmail())
                .putMetadata("usuarioId", String.valueOf(usuario.getId()))
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(pricePagoUnico)
                        .setQuantity(1L)
                        .build())
                .build();

        Session session = stripeClient.v1().checkout().sessions().create(params);
        return session.getUrl();
    }

    public String crearCheckoutSuscripcion(
            Usuario usuario, PlanSuscripcion plan) throws StripeException {
        String priceId = resolverPrecio(plan);

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(urlExito)
                .setCancelUrl(urlCancelado)
                .setCustomerEmail(usuario.getEmail())
                .putMetadata("usuarioId", String.valueOf(usuario.getId()))
                .putMetadata("plan", plan.name())
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(priceId)
                        .setQuantity(1L)
                        .build())
                .build();

        Session session = stripeClient.v1().checkout().sessions().create(params);
        return session.getUrl();
    }

    private String resolverPrecio(PlanSuscripcion plan) {
        return switch (plan) {
            case INDIVIDUAL -> pricePlanIndividual;
            default -> throw new PlanNoDisponibleException(
                    "El plan " + plan
                    + " todavía no está disponible para pago en línea");
        };
    }
}
