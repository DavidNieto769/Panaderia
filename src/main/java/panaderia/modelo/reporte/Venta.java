package panaderia.modelo.reporte;

import panaderia.modelo.Producto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Venta implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Producto> productos;
    private String fecha; // Fecha en la que se realizó la venta

    public Venta() {
        this.productos = new ArrayList<>();
        this.fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // Asigna la fecha actual al crear la venta
    }

    public Venta(Date fecha, List<Producto> productos) {
        this.fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fecha);
        this.productos = productos != null ? new ArrayList<>(productos) : new ArrayList<>();
    }

    // Agrega un producto a la lista de la venta
    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    // Devuelve la lista completa de productos en la venta
    public List<Producto> getProductos() {
        return productos;
    }

    public String getFecha() {
        return fecha;
    }

    // Busca un producto en la venta por su nombre
    public Producto buscarProductoPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    // Calcula el valor total de la venta sumando (precio * cantidad) de cada producto
    public double calcularValorTotal() {
        double total = 0;
        for (Producto p : productos) {
            total += p.getPrecioVenta() * p.getCantidad();
        }
        return total;
    }

    // Imprime en consola los productos de la venta con su descripción y cantidad
    public void listarProductos() {
        if (productos.isEmpty()) {
            System.out.println("Inventario vacío.");
        } else {
            for (Producto p : productos) {
                System.out.println(p.getDescripcion() + " - Cantidad: " + p.getCantidad());
            }
        }
    }

    // Filtra productos cuyo nombre contenga una subcadena específica
    public List<Producto> filtrarPorNombre(String nombreParcial) {
        return productos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombreParcial.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Filtra productos cuyo precio sea menor o igual al máximo especificado
    public List<Producto> filtrarPorPrecioMaximo(double precioMaximo) {
        return productos.stream()
                .filter(p -> p.getPrecioVenta() <= precioMaximo)
                .collect(Collectors.toList());
    }

    // Filtra productos cuya cantidad sea igual o superior a la mínima requerida
    public List<Producto> filtrarPorCantidadMinima(int cantidadMinima) {
        return productos.stream()
                .filter(p -> p.getCantidad() >= cantidadMinima)
                .collect(Collectors.toList());
    }
}
