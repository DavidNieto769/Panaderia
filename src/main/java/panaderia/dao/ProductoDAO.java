package panaderia.dao;

import panaderia.modelo.Producto;
import java.util.List;

public interface ProductoDAO {
    void insertar(Producto producto, List<Producto> productos); // Inserta un producto, reemplazando si ya existe uno con el mismo nombre.
    List<Producto> obtenerTodos();
    Producto buscarPorNombre(String nombre);
    void actualizarCantidad(String nombre, int nuevaCantidad);
    void eliminar(Producto producto);
    void guardarTodos(List<Producto> productos);
}
