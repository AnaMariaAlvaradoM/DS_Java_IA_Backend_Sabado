import config.ConexionBD;
import dao.ClienteDAO;
import dao.ClienteDAOImpl;
import model.Cliente;

import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }

        ClienteDAO clienteDAO = new ClienteDAOImpl();

        System.out.println("=== INSERTAR ===");
        clienteDAO.insertar(new Cliente("Laura Gomez", "laura@mail.com", "Bogota"));
        clienteDAO.insertar(new Cliente("Pedro Diaz", "pedro@mail.com", "Medellin"));

        System.out.println("\n=== LISTAR ===");
        clienteDAO.listar().forEach(System.out::println);

        System.out.println("\n=== ACTUALIZAR ===");
        clienteDAO.actualizar(
                new Cliente(1, "Laura Gomez Ruiz", "laura.ruiz@mail.com", "Cali")
        );

        System.out.println("\n=== LISTAR TRAS ACTUALIZAR ===");
        clienteDAO.listar().forEach(System.out::println);

        System.out.println("\n=== ELIMINAR ===");
        clienteDAO.eliminar(2);

        System.out.println("\n=== LISTAR FINAL ===");
        clienteDAO.listar().forEach(System.out::println);
    }
}