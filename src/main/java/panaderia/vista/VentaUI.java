package panaderia.vista;

import javax.swing.*;
import java.awt.*;

import panaderia.controlador.utilidades.FiltroTexto;
import panaderia.modelo.Producto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;

import panaderia.modelo.reporte.Venta;
import panaderia.persistencia.ArchivoBinario;

public class VentaUI {
    java.util.List<String> productosSeleccionados = new java.util.ArrayList<>();

    public static void mostrar(Component parentComponent, List<Producto> productosDisponibles, VentaCallback callback) {
        if (productosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "No hay productos disponibles para vender.");
            return;
        }

        while (true) {

            JTextField campoNumero = new JTextField();
            ((AbstractDocument) campoNumero.getDocument()).setDocumentFilter(new FiltroTexto.SoloNumeros());

            int resultado = JOptionPane.showConfirmDialog(
                    null,
                    campoNumero,
                    "¿Cuántos productos diferentes desea vender?",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (resultado != JOptionPane.OK_OPTION) break;

            String input = campoNumero.getText().trim();
            int numeroProductos;
            try {
                numeroProductos = Integer.parseInt(input);
                if (numeroProductos <= 0) throw new NumberFormatException("Debe ser mayor que cero.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Número inválido: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            JPanel panel = new JPanel(new GridLayout(numeroProductos * 2, 2, 5, 5));
            JComboBox<String>[] combos = new JComboBox[numeroProductos];
            JTextField[] camposCantidad = new JTextField[numeroProductos];
            java.util.Set<String> nombresSeleccionados = new java.util.HashSet<>();

            for (int i = 0; i < numeroProductos; i++) {
                combos[i] = new JComboBox<>();
                camposCantidad[i] = new JTextField();

                panel.add(new JLabel("Producto #" + (i + 1) + ":"));
                panel.add(combos[i]);
                panel.add(new JLabel("Cantidad:"));
                panel.add(camposCantidad[i]);
                ((AbstractDocument) camposCantidad[i].getDocument()).setDocumentFilter(new FiltroTexto.SoloNumeros());
            }

// Función para actualizar dinámicamente los combos
            Runnable actualizarCombos = () -> {
                Set<String> seleccionados = new HashSet<>();
                for (JComboBox<String> combo : combos) {
                    if (combo.getSelectedItem() != null) {
                        seleccionados.add(combo.getSelectedItem().toString());
                    }
                }

                for (int i = 0; i < combos.length; i++) {
                    String seleccionActual = (String) combos[i].getSelectedItem();

                    combos[i].removeAllItems();
                    for (Producto p : productosDisponibles) {
                        String nombre = p.getNombre();
                        if (!seleccionados.contains(nombre) || nombre.equals(seleccionActual)) {
                            combos[i].addItem(nombre);
                        }
                    }

                    combos[i].setSelectedItem(seleccionActual); // Mantener selección si aún está disponible
                }
            };

// Agregamos listener a todos los combos
            for (JComboBox<String> combo : combos) {
                combo.addActionListener(e -> actualizarCombos.run());
            }

// Llenamos inicialmente los combos
            actualizarCombos.run();


            int opcion = JOptionPane.showConfirmDialog(parentComponent, panel, "Registrar Ventas", JOptionPane.OK_CANCEL_OPTION);
            if (opcion != JOptionPane.OK_OPTION) break;

            boolean error = false;
            for (int i = 0; i < numeroProductos; i++) {
                String productoSeleccionado = (String) combos[i].getSelectedItem();
                String cantidadTexto = camposCantidad[i].getText().trim();

                try {
                    int cantidad = Integer.parseInt(cantidadTexto);
                    if (cantidad <= 0) throw new NumberFormatException("Debe ser mayor que cero.");

                    Producto p = productosDisponibles.stream()
                            .filter(prod -> prod.getNombre().equals(productoSeleccionado))
                            .findFirst().orElse(null);

                    if (p == null) throw new Exception("Producto no encontrado.");
                    if (p.getCantidad() < cantidad) {
                        JOptionPane.showMessageDialog(parentComponent,
                                "No hay suficiente stock para \"" + p.getNombre() + "\". Stock disponible: " + p.getCantidad(),
                                "Stock insuficiente",
                                JOptionPane.ERROR_MESSAGE);
                        error = true;
                        break;
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parentComponent, "Cantidad inválida para producto #" + (i + 1) + ": " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    error = true;
                    break;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(parentComponent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    error = true;
                    break;
                }
            }

            if (error) continue; // vuelve a mostrar el formulario

            // Todas las validaciones fueron correctas, realizamos las ventas
            for (int i = 0; i < numeroProductos; i++) {
                String productoSeleccionado = (String) combos[i].getSelectedItem();
                int cantidad = Integer.parseInt(camposCantidad[i].getText().trim());
                callback.realizarVenta(productoSeleccionado, cantidad);
            }

            JOptionPane.showMessageDialog(parentComponent, "Ventas registradas correctamente.");

            int continuar = JOptionPane.showConfirmDialog(parentComponent, "¿Desea registrar otra venta?", "Continuar", JOptionPane.YES_NO_OPTION);
            if (continuar != JOptionPane.YES_OPTION) break;
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
