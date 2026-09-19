public class Cliente extends Usuario implements Notificable, Facturar{
    private int puntos;

    public Cliente(String nombre, String email) {
        super(nombre, email);
        this.puntos = 0;
    }

    @Override
    public String panelInicio() {
        return "desde cliente";
    }

    @Override
    public String recibirNotificacion(String mensaje) {
        return "";
    }

    @Override
    public String hacerFactura(String mensaje) {
        return "haicnedo factura";
    }
}
