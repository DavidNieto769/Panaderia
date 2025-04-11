package panaderia.dao;

import panaderia.modelo.Producto;
import panaderia.persistencia.ArchivoBinario;
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
        ArchivoBinario.guardar(archivo, productos); // Persiste la lista actualizada de productos.
    }

    @Override
    public void eliminar(Producto producto) {
        List<Producto> productos = ArchivoBinario.cargar(archivo);
        productos.removeIf(p -> p.getNombre().equalsIgnoreCase(producto.getNombre()));
        ArchivoBinario.guardar(archivo, productos); // Guarda el nuevo estado después de la eliminación.
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
                .orElse(null); // Devuelve null si no encuentra un producto con el nombre indicado.
    }

    @Override
    public void actualizarCantidad(String nombre, int nuevaCantidad) {
        List<Producto> productos = obtenerTodos();
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                p.setCantidad(nuevaCantidad); // Actualiza la cantidad del producto especificado.
                break;
            }
        }
        ArchivoBinario.guardar(archivo, productos);
    }


    public void guardarTodos(List<Producto> productos) {
        ArchivoBinario.guardar(archivo, productos);
    }
}
