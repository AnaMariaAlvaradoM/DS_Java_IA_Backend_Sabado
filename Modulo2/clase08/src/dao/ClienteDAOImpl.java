
package dao;

import config.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public void insertar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nombre, email, ciudad) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getCiudad());
            System.out.println("Filas insertadas: " + ps.executeUpdate());
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
        }
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nombre, email, ciudad FROM cliente";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("ciudad")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }
        return clientes;
    }

    @Override
    public void actualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nombre = ?, email = ?, ciudad = ? WHERE id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getCiudad());
            ps.setInt(4, cliente.getId());
            System.out.println("Filas actualizadas: " + ps.executeUpdate());
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            System.out.println("Filas eliminadas: " + ps.executeUpdate());
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }
}
//package dao;
//
//import config.ConexionBD;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import model.Cliente;
//
//public class ClienteDAOImpl implements ClienteDAO {
//
//    @Override
//    public void insertar(Cliente cliente) {
//        String sql = "INSERT INTO cliente (nombre, email, ciudad) VALUES (?, ?, ?)";
//
//        try (Connection con = ConexionBD.obtenerConexion();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, cliente.getNombre());
//            ps.setString(2, cliente.getEmail());
//            ps.setString(3, cliente.getCiudad());
//
//            int filas = ps.executeUpdate();
//            System.out.println("Filas insertadas: " + filas);
//        } catch (SQLException e) {
//            System.out.println("Error al insertar: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<Cliente> listar() {
//        List<Cliente> clientes = new ArrayList<>();
//        String sql = "SELECT id, nombre, email, ciudad FROM cliente";
//
//        try (Connection con = ConexionBD.obtenerConexion();
//             PreparedStatement ps = con.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                Cliente cliente = new Cliente(
//                        rs.getInt("id"),
//                        rs.getString("nombre"),
//                        rs.getString("email"),
//                        rs.getString("ciudad")
//                );
//                clientes.add(cliente);
//            }
//        } catch (SQLException e) {
//            System.out.println("Error al listar: " + e.getMessage());
//        }
//
//        return clientes;
//    }
//
//    @Override
//    public void actualizar(Cliente cliente) {
//        String sql = "UPDATE cliente SET nombre = ?, email = ?, ciudad = ? WHERE id = ?";
//
//        try (Connection con = ConexionBD.obtenerConexion();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, cliente.getNombre());
//            ps.setString(2, cliente.getEmail());
//            ps.setString(3, cliente.getCiudad());
//            ps.setInt(4, cliente.getId());
//
//            int filas = ps.executeUpdate();
//            System.out.println("Filas actualizadas: " + filas);
//        } catch (SQLException e) {
//            System.out.println("Error al actualizar: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void eliminar(int id) {
//        String sql = "DELETE FROM cliente WHERE id = ?";
//
//        try (Connection con = ConexionBD.obtenerConexion();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setInt(1, id);
//            int filas = ps.executeUpdate();
//            System.out.println("Filas eliminadas: " + filas);
//        } catch (SQLException e) {
//            System.out.println("Error al eliminar: " + e.getMessage());
//        }
//    }
//}