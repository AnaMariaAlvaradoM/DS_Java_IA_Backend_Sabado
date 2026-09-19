public class Vendedor extends Usuario{
    private String codigo;

    public Vendedor(String nombre, String email, String codigo) {
        super(nombre, email);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String panelInicio() {
        return "Panel de Vendedor: publicar y gestionar mis productos";
    }

    public String saludar(){
        return "hola";
    }

}
