package panaderia.controlador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import panaderia.modelo.Producto;
import panaderia.modelo.reporte.*;

public class ControladorInventario {
 
    private Inventario inventario;

    public ControladorInventario() {
        this.inventario = new Inventario();

        // Se cargan automáticamente los productos desde el archivo productos.csv
        List<Producto> productosCargados = InventarioSerializable.cargarProductosSerializable();
        for (Producto p : productosCargados) {
            inventario.agregarProducto(p);
        }
    }

    // Método para agregar un producto al inventario
    public void agregarProducto(Producto producto) {
        inventario.agregarProducto(producto);
    }

    // Método que devuelve la lista completa de productos del inventario
    public List<Producto> obtenerProductos() {
        return inventario.getProductos();
    }

    // Método para aplicar filtros sobre los productos del inventario por nombre, precio máximo o cantidad mínima
    public List<Producto> filtrar(String nombre, String precioMax, String cantidadMin) {
        List<Producto> filtrados = inventario.getProductos();

        // Se filtra por nombre si no está vacío
        if (!nombre.isEmpty()) {
            filtrados = inventario.filtrarPorNombre(nombre);
        }

        // Se filtra por precio máximo si se proporciona un valor válido
        if (!precioMax.isEmpty()) {
            try {
                double max = Double.parseDouble(precioMax);
                filtrados = inventario.filtrarPorPrecioMaximo(max);
            } catch (NumberFormatException e) {
                System.out.println("Precio inválido.");
            }
        }

        // Se filtra por cantidad mínima si se proporciona un valor válido
        if (!cantidadMin.isEmpty()) {
            try {
                int min = Integer.parseInt(cantidadMin);
                filtrados = inventario.filtrarPorCantidadMinima(min);
            } catch (NumberFormatException e) {
                System.out.println("Cantidad inválida.");
            }
        }

        return filtrados;
    }

    // Método que guarda el estado actual del inventario en un archivo CSV especificado por el usuario
    public void guardarSerializable() {
        InventarioSerializable.guardarProductosSerializable(inventario.getProductos());
    }



    // Método que genera un reporte del inventario y lo guarda con la fecha y hora actuales en el nombre del archivo
    public void guardarReporteConFecha() {
        String fecha = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String nombreArchivo = "reporteInventario-" + fecha + ".ser";
        InventarioSerializable.guardarProductosSerializable(inventario.getProductos());
    }

    // Método que crea una copia del reporte de ventas con una marca de tiempo para conservar versiones históricas
    public void guardarReporteVentasConFecha() {
        String fecha = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String nombreCopia = "reporteVentas-" + fecha + ".ser";

        try {
            Path origen = Paths.get("reporteVentas.ser");
            Path destino = Paths.get(nombreCopia);
            Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    public void crearProducto(Component parentComponent, Runnable actualizarVista) {
        try {
            Producto producto = FormularioProducto.mostrarDialogo(parentComponent);
            if (producto != null) {
                agregarProducto(producto);
                guardarSerializable();
                if (actualizarVista != null) actualizarVista.run();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentComponent, "Error al crear producto: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }




}
