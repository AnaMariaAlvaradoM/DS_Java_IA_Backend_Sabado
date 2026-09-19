public class Administrador extends Usuario{
    private int nivel;

    public Administrador(String nombre, String email, int nivel) {
        super(nombre, email);
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    @Override
    public String panelInicio() {
        return "Panel de Admin: gestionar usuarios y reportes";
    }
}
