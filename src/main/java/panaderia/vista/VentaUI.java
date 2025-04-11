package panaderia.vista;

import javax.swing.*;
import java.awt.*;
import panaderia.modelo.Producto;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import panaderia.modelo.reporte.Venta;
import panaderia.persistencia.ArchivoBinario;

public class VentaUI {

    public static void mostrar(Component parentComponent, List<Producto> productosDisponibles, VentaCallback callback) {
        if (productosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "No hay productos disponibles para vender.");
            return;
        }

        JComboBox<String> combo = new JComboBox<>();
        for (Producto p : productosDisponibles) {
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
                if (cantidad <= 0) throw new NumberFormatException("La cantidad debe ser mayor que cero.");

                callback.realizarVenta(productoSeleccionado, cantidad);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parentComponent, "Cantidad inválida: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    // Para crear un nuevo producto
    public static Producto mostrarDialogoNuevo(Component parent) throws Exception {
        return FormularioProducto.mostrarDialogo(parent, null);
    }

    // Para editar un producto existente
    public static Producto mostrarDialogoEditar(Component parent, Producto productoExistente) throws Exception {
        return FormularioProducto.mostrarDialogo(parent, productoExistente);
    }


    public static void mostrarError(Component parentComponent, String mensaje) {
        JOptionPane.showMessageDialog(parentComponent, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @FunctionalInterface
    public interface VentaCallback {
        void realizarVenta(String nombreProducto, int cantidad);
    }


    public static void mostrarMensaje(Component parent, String mensaje) {
        JOptionPane.showMessageDialog(parent, mensaje);
    }


    public static void mostrarTablaVentas(JFrame frame) {
        List<Venta> ventas = ArchivoBinario.cargar("reporteVentas.ser");
        DefaultTableModel modeloVentas = new DefaultTableModel(new Object[]{
                "Fecha", "Producto", "Cantidad", "Precio Unitario", "Total"
        }, 0);

        if (ventas != null) {
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
        }

        if (modeloVentas.getRowCount() == 0) {
            mostrarMensaje(frame, "No hay ventas registradas.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTable tablaVentas = new JTable(modeloVentas);
        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        JOptionPane.showMessageDialog(frame, scrollPane, "Ventas realizadas", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void mostrarMensaje(Component parent, String mensaje, int tipo) {
        JOptionPane.showMessageDialog(parent, mensaje, "Información", tipo);
    }




}
