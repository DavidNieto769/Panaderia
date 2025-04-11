package panaderia.dao;

import panaderia.modelo.Producto;
import panaderia.persistencia.ArchivoBinario;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public abstract class AbstractProductoDAO implements ProductoDAO {

    private final String archivo;

    public AbstractProductoDAO(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public void insertar(Producto producto, List<Producto> productos) {
        productos.removeIf(p -> p.getNombre().equalsIgnoreCase(producto.getNombre()));
        productos.add(producto);
        ArchivoBinario.guardar(archivo, productos);
    }

    @Override
    public void eliminar(Producto producto) {
        List<Producto> productos = ArchivoBinario.cargar(archivo);
        productos.removeIf(p -> p.getNombre().equalsIgnoreCase(producto.getNombre()));
        ArchivoBinario.guardar(archivo, productos); // guardar el nuevo estado
    }

    @Override
    public List<Producto> obtenerTodos() {
        return ArchivoBinario.cargar(archivo);
    }

    @Override
    public Producto buscarPorNombre(String nombre) {
        return obtenerTodos().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void actualizarCantidad(String nombre, int nuevaCantidad) {
        List<Producto> productos = obtenerTodos();
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                p.setCantidad(nuevaCantidad);
                break;
            }
        }
        ArchivoBinario.guardar(archivo, productos);
    }

    // Si aún necesitas este método por fuera
    public void guardarTodos(List<Producto> productos) {
        ArchivoBinario.guardar(archivo, productos);
    }
}





