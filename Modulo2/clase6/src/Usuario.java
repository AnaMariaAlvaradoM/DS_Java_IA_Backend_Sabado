public abstract class Usuario {
    protected String nombre;
    protected String email;

    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public abstract String panelInicio();

    public String saludar(){
        return "hola";
    }



    @Override
    public String toString() {
        return nombre + " <" + email + ">";
    }
}