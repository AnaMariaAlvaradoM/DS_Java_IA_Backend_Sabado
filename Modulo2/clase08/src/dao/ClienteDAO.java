package dao;

import java.util.List;
import model.Cliente;

public interface ClienteDAO {
    void insertar(Cliente cliente);
    List<Cliente> listar();
    void actualizar(Cliente cliente);
    void eliminar(int id);
}
