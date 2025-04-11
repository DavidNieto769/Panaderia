package panaderia.vista;

import javax.swing.*;
import java.awt.*;

import panaderia.vista.utilidadesvista.FiltroTexto;
import panaderia.modelo.Producto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;

import panaderia.modelo.reporte.Venta;
import panaderia.persistencia.ArchivoBinario;

public class VentaUI {


    // Método para solicitar al usuario cuántos productos diferentes desea vender
    private static int pedirNumeroProductosDiferentes(Component parentComponent) {
        // Se crea un campo de texto y se le aplica un filtro para que solo acepte números
        JTextField campoNumero = new JTextField();
        ((AbstractDocument) campoNumero.getDocument()).setDocumentFilter(new FiltroTexto.SoloNumeros());

        // Se muestra un cuadro de diálogo para que el usuario ingrese el número de productos
        int resultado = JOptionPane.showConfirmDialog(
                parentComponent,
                campoNumero,
                "¿Cuántos productos diferentes desea vender?",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // Si el usuario cancela la operación, se retorna -1
        if (resultado != JOptionPane.OK_OPTION) return -1;

        // Se obtiene y limpia el texto ingresado
        String input = campoNumero.getText().trim();
        try {
            // Se intenta convertir el texto en un entero
            int numeroProductos = Integer.parseInt(input);
            // Si el número es menor o igual a cero, se lanza una excepción
            if (numeroProductos <= 0) throw new NumberFormatException("Debe ser mayor que cero.");
            return numeroProductos;
        } catch (NumberFormatException e) {
            // Si ocurre un error al convertir o el número no es válido, se muestra un mensaje de error
            JOptionPane.showMessageDialog(parentComponent, "Número inválido: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    // Método para actualizar el contenido de varios JComboBox asegurando que no se repitan productos seleccionados
    private static void actualizarCombos(JComboBox<String>[] combos, List<Producto> productosDisponibles) {
        Set<String> seleccionados = new HashSet<>();

        // Se recorren todos los combos para registrar los productos ya seleccionados
        for (JComboBox<String> combo : combos) {
            if (combo.getSelectedItem() != null) {
                seleccionados.add(combo.getSelectedItem().toString());
            }
        }

        // Para cada combo, se reconstruye la lista de opciones
        for (int i = 0; i < combos.length; i++) {
            String seleccionActual = (String) combos[i].getSelectedItem(); // Se guarda la selección actual

            combos[i].removeAllItems(); // Se limpian las opciones del combo

            for (Producto p : productosDisponibles) {
                String nombre = p.getNombre();
                // Se agrega el producto si no está seleccionado por otro combo,
                // o si es el que estaba seleccionado actualmente (para mantenerlo visible)
                if (!seleccionados.contains(nombre) || nombre.equals(seleccionActual)) {
                    combos[i].addItem(nombre);
                }
            }

            // Se restablece la selección anterior si aún es válida
            combos[i].setSelectedItem(seleccionActual);
        }
    }

    // Método para validar que los productos seleccionados y sus cantidades sean correctos
    private static boolean validarProductosSeleccionados(Component parentComponent,
                                                         int numeroProductos,
                                                         JComboBox<String>[] combos,
                                                         JTextField[] camposCantidad,
                                                         List<Producto> productosDisponibles) {
        for (int i = 0; i < numeroProductos; i++) {
            // Se obtiene el producto y la cantidad ingresada por el usuario
            String productoSeleccionado = (String) combos[i].getSelectedItem();
            String cantidadTexto = camposCantidad[i].getText().trim();

            try {
                // Se convierte el texto a número y se valida que sea mayor a cero
                int cantidad = Integer.parseInt(cantidadTexto);
                if (cantidad <= 0) throw new NumberFormatException("Debe ser mayor que cero.");

                // Se busca el producto seleccionado en la lista de productos disponibles
                Producto p = productosDisponibles.stream()
                        .filter(prod -> prod.getNombre().equals(productoSeleccionado))
                        .findFirst().orElse(null);

                // Si no se encuentra el producto, se lanza una excepción
                if (p == null) throw new Exception("Producto no encontrado.");

                // Se valida si hay suficiente stock disponible
                if (p.getCantidad() < cantidad) {
                    JOptionPane.showMessageDialog(parentComponent,
                            "No hay suficiente stock para \"" + p.getNombre() + "\". Stock disponible: " + p.getCantidad(),
                            "Stock insuficiente",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }

            } catch (NumberFormatException e) {
                // Error en la conversión de texto a número
                JOptionPane.showMessageDialog(parentComponent,
                        "Cantidad inválida para producto #" + (i + 1) + ": " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } catch (Exception e) {
                // Otros errores, como producto no encontrado
                JOptionPane.showMessageDialog(parentComponent,
                        e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Si todas las validaciones pasan, se retorna true
        return true;
    }


    public static void mostrar(Component parentComponent, List<Producto> productosDisponibles, VentaCallback callback) {
        if (productosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(parentComponent, "No hay productos disponibles para vender.");
            return;
        }

        while (true) {

            int numeroProductos = pedirNumeroProductosDiferentes(parentComponent);
            if (numeroProductos <= 0) continue;

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
            Runnable actualizarCombos = () -> actualizarCombos(combos, productosDisponibles);


            // Agregar listener a todos los combos
            for (JComboBox<String> combo : combos) {
                combo.addActionListener(e -> actualizarCombos.run());
            }

            // Llenar inicialmente los combos
            actualizarCombos.run();

            int opcion = JOptionPane.showConfirmDialog(parentComponent, panel, "Registrar Ventas", JOptionPane.OK_CANCEL_OPTION);
            if (opcion != JOptionPane.OK_OPTION) break;

            boolean esValido = validarProductosSeleccionados(parentComponent, numeroProductos, combos, camposCantidad, productosDisponibles);
            if (!esValido) continue;


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
