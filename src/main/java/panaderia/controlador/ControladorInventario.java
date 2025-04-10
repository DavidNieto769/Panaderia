package panaderia.controlador;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import panaderia.modelo.Galleta;
import panaderia.modelo.Pan;
import panaderia.modelo.Producto;
import panaderia.modelo.reporte.*;
import panaderia.vista.FormularioProducto;

import javax.swing.*;

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
        // Iniciar con todos los productos del inventario
        List<Producto> filtrados = inventario.getProductos();

        // Se filtra por nombre si no está vacío
        if (!nombre.isEmpty()) {
            filtrados = filtrados.stream()
                    .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Se filtra por precio máximo si se proporciona un valor válido
        if (!precioMax.isEmpty()) {
            try {
                double max = Double.parseDouble(precioMax);
                filtrados = filtrados.stream()
                        .filter(p -> p.getPrecioVenta() <= max)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                System.out.println("Precio inválido.");
            }
        }

        // Se filtra por cantidad mínima si se proporciona un valor válido
        if (!cantidadMin.isEmpty()) {
            try {
                int min = Integer.parseInt(cantidadMin);
                filtrados = filtrados.stream()
                        .filter(p -> p.getCantidad() >= min)
                        .collect(Collectors.toList());
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


    public boolean registrarVenta(String nombreProducto, int cantidadVendida, Component parentComponent) {
        Producto producto = inventario.getProductos().stream()
                .filter(p -> p.getNombre().equals(nombreProducto))
                .findFirst()
                .orElse(null);

        if (producto == null) {
            JOptionPane.showMessageDialog(parentComponent, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (producto.getCantidad() < cantidadVendida) {
            JOptionPane.showMessageDialog(parentComponent, "No hay suficiente stock para realizar esta venta.", "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Actualizar inventario
        producto.setCantidad(producto.getCantidad() - cantidadVendida);
        guardarSerializable();

        // Clonar el producto vendido según su tipo
        Producto vendido;
        if (producto instanceof Pan) {
            vendido = new Pan(producto.getNombre(), producto.getPrecioVenta(), producto.getCostoProduccion(), cantidadVendida, producto.isExtra());
        } else if (producto instanceof Galleta) {
            vendido = new Galleta(producto.getNombre(), producto.getPrecioVenta(), producto.getCostoProduccion(), cantidadVendida, producto.isExtra());
        } else {
            JOptionPane.showMessageDialog(parentComponent, "Tipo de producto desconocido.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Registrar la venta
        Venta venta = new Venta();
        venta.agregarProducto(vendido);
        VentasSerializable.guardarVenta(venta);

        return true;
    }


    public void mostrarDialogoVenta(Component parentComponent, Runnable callbackActualizarTabla) {
        // Se obtiene la lista de productos disponibles (con cantidad mayor a 0)
        List<Producto> disponibles = inventario.getProductos().stream()
                .filter(p -> p.getCantidad() > 0)
                .toList();

        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "No hay productos disponibles para vender.");
            return;
        }

        // ComboBox con los nombres de productos
        JComboBox<String> combo = new JComboBox<>();
        for (Producto p : disponibles) {
            combo.addItem(p.getNombre());
        }

        JTextField campoCantidad = new JTextField();

        JPanel panelVenta = new JPanel(new GridLayout(2, 2));
        panelVenta.add(new JLabel("Producto:"));
        panelVenta.add(combo);
        panelVenta.add(new JLabel("Cantidad a vender:"));
        panelVenta.add(campoCantidad);

        int opcion = JOptionPane.showConfirmDialog(
                parentComponent,
                panelVenta,
                "Venta de producto",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcion == JOptionPane.OK_OPTION) {
            String productoSeleccionado = (String) combo.getSelectedItem();
            String cantidadTexto = campoCantidad.getText().trim();

            try {
                int cantidad = Integer.parseInt(cantidadTexto);
                if (cantidad <= 0) {
                    throw new NumberFormatException("La cantidad debe ser mayor que cero.");
                }

                boolean exito = registrarVenta(productoSeleccionado, cantidad, parentComponent);
                if (exito) {
                    callbackActualizarTabla.run();
                    JOptionPane.showMessageDialog(parentComponent, "Venta realizada con éxito.");
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parentComponent, "Cantidad inválida: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public List<String[]> obtenerVentas() {
        List<String[]> ventas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("reporteVentas.ser"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    ventas.add(datos);
                }
            }
        } catch (IOException e) {
            // Puedes registrar el error si lo deseas o simplemente devolver la lista vacía
            e.printStackTrace();
        }

        return ventas;
    }

}
