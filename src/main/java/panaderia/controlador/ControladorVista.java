package panaderia.controlador;

import panaderia.modelo.Producto;
import panaderia.modelo.reporte.Venta;
import panaderia.modelo.reporte.VentasSerializable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.function.Consumer;

public class ControladorVista {
    private final ControladorInventario controlador;

    public ControladorVista(ControladorInventario controlador) {
        this.controlador = controlador;
    }

    public void agregarProducto(JFrame frame, Runnable postAccion) {
        controlador.crearProducto(frame, postAccion);
    }

    public void venderProducto(JFrame frame, Runnable postAccion) {
        controlador.mostrarDialogoVenta(frame, postAccion);
    }

    public void guardarReportes(JFrame frame) {
        controlador.guardarReporteConFecha();
        controlador.guardarReporteVentasConFecha();
        JOptionPane.showMessageDialog(frame, "Reporte generado exitosamente.");
    }

    public void mostrarVentas(JFrame frame) {
        DefaultTableModel modeloVentas = new DefaultTableModel(new Object[]{
                "Fecha", "Producto", "Cantidad", "Precio Unitario", "Total"
        }, 0);

        List<Venta> ventas = VentasSerializable.cargarVentas();
        for (Venta v : ventas) {
            for (Producto p : v.getProductos()) {
                Object[] fila = new Object[]{
                        v.getFecha(),
                        p.getNombre(),
                        p.getCantidad(),
                        p.getPrecioVenta(),
                        p.getCantidad() * p.getPrecioVenta()
                };
                modeloVentas.addRow(fila);
            }
        }

        if (modeloVentas.getRowCount() == 0) {
            JOptionPane.showMessageDialog(frame, "No hay ventas registradas.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTable tablaVentas = new JTable(modeloVentas);
        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        JOptionPane.showMessageDialog(frame, scrollPane, "Ventas realizadas", JOptionPane.INFORMATION_MESSAGE);
    }

    public List<Producto> aplicarFiltros(String nombre, String precio, String cantidad) {
        return controlador.filtrar(nombre.trim(), precio.trim(), cantidad.trim());
    }

    public List<Producto> obtenerProductos() {
        return controlador.obtenerProductos();
    }
}
