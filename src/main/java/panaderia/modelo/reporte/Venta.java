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
}
