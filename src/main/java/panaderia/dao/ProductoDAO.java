package panaderia.dao;

import panaderia.modelo.Producto;
import java.util.List;

public interface ProductoDAO {
    void insertar(Producto producto, List<Producto> productos);
    List<Producto> obtenerTodos();
    Producto buscarPorNombre(String nombre);
    void actualizarCantidad(String nombre, int nuevaCantidad);

}