package panaderia.modelo.reporte;

import java.util.ArrayList;
import java.util.List;

import panaderia.modelo.Producto;

public class Inventario {

    private List<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    // Agrega un producto al inventario.
    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    // Elimina un producto del inventario según su nombre (ignorando mayúsculas/minúsculas).
    public void eliminarProducto(String nombre) {
        productos.removeIf(p -> p.getNombre().equalsIgnoreCase(nombre));
    }

    // Edita un producto eliminando el existente y agregando el nuevo con los cambios.
    public void editarProducto(Producto producto) {
        eliminarProducto(producto.getNombre());
        agregarProducto(producto);
    }

    // Busca un producto en el inventario por su nombre.
    public Producto buscarProductoPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    // Retorna una copia de la lista de productos.
    public List<Producto> obtenerProductos() {
        return new ArrayList<>(productos);
    }

    // Elimina todos los productos del inventario.
    public void limpiar() {
        productos.clear();
    }

    // Devuelve la lista interna de productos (sin copia).
    public List<Producto> getProductos() {
        return productos;
    }

    // Calcula el valor total del inventario multiplicando precio por cantidad.
    public double calcularValorTotal() {
        double total = 0;
        for (Producto p : productos) {
            total += p.getPrecioVenta() * p.getCantidad();
        }
        return total;
    }

    // Imprime en consola la descripción y cantidad de cada producto en el inventario.
    public void listarProductos() {
        if (productos.isEmpty()) {
            System.out.println("Inventario vacío.");
        } else {
            for (Producto p : productos) {
                System.out.println(p.getDescripcion() + " - Cantidad: " + p.getCantidad());
            }
        }
    }
}
