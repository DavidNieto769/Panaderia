package panaderia.dao;

import java.util.ArrayList;
import java.util.List;
import panaderia.modelo.Producto;
import panaderia.persistencia.ArchivoBinario;

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
    public List<Producto> obtenerTodos() {
        return new ArrayList<>(ArchivoBinario.cargar(archivo));
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
}
