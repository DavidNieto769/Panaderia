package panaderia.modelo.reporte;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import panaderia.modelo.Producto;


public class Inventario {

    private List<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }


    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void limpiar() {
        productos.clear();
    }

    public Producto buscarProductoPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    public double calcularValorTotal() {
        double total = 0;
        for (Producto p : productos) {
            total += p.getPrecioVenta() * p.getCantidad();
        }
        return total;
    }

    public void listarProductos() {
        if (productos.isEmpty()) {
            System.out.println("Inventario vacío.");
        } else {
            for (Producto p : productos) {
                System.out.println(p.getDescripcion() + " - Cantidad: " + p.getCantidad());
            }
        }
    }

    public void editarProducto(Producto producto) {
        eliminarProducto(producto.getNombre());
        agregarProducto(producto);
    }

    public void eliminarProducto(String nombre) {
        productos.removeIf(p -> p.getNombre().equalsIgnoreCase(nombre));
    }

}
