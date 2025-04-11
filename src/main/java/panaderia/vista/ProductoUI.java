package panaderia.vista;

import panaderia.controlador.ControladorInventario;
import panaderia.modelo.Producto;
import panaderia.vista.FormularioProducto;

import javax.swing.*;
import java.util.List;

public class ProductoUI {

    public static void eliminarProducto(JFrame frame, ControladorInventario controlador, Runnable postAccion) {
        List<Producto> productos = controlador.obtenerProductos();

        if (productos.isEmpty()) {
            mostrarMensaje(frame, "No hay productos para eliminar.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] nombres = productos.stream().map(Producto::getNombre).toArray(String[]::new);

        String nombreSeleccionado = (String) JOptionPane.showInputDialog(
                frame,
                "Seleccione el producto a eliminar:",
                "Eliminar Producto",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombres,
                nombres.length > 0 ? nombres[0] : null
        );

        if (nombreSeleccionado != null) {
            Producto producto = controlador.obtenerProductoPorNombre(nombreSeleccionado);
            if (producto != null) {
                controlador.eliminarProducto(producto);
                mostrarMensaje(frame, "Producto eliminado correctamente.");
                postAccion.run();
            } else {
                mostrarMensaje(frame, "Producto no encontrado.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void editarProducto(JFrame frame, ControladorInventario controlador, Runnable postAccion) {
        List<Producto> productos = controlador.obtenerProductos();

        if (productos.isEmpty()) {
            mostrarMensaje(frame, "No hay productos para editar.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] nombres = productos.stream().map(Producto::getNombre).toArray(String[]::new);

        String nombreSeleccionado = (String) JOptionPane.showInputDialog(
                frame,
                "Seleccione el producto a editar:",
                "Editar Producto",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombres,
                nombres.length > 0 ? nombres[0] : null
        );

        if (nombreSeleccionado != null) {
            Producto original = controlador.obtenerProductoPorNombre(nombreSeleccionado);
            if (original != null) {
                try {
                    String nombreAnterior = original.getNombre();

                    Producto editado = FormularioProducto.mostrarDialogo(frame, original);
                    if (editado != null) {
                        controlador.eliminarProducto(original);
                        controlador.agregarProducto(editado);
                        mostrarMensaje(frame, "Producto editado correctamente.");
                        postAccion.run();
                    }
                } catch (Exception ex) {
                    mostrarMensaje(frame, "Error al editar producto: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                mostrarMensaje(frame, "Producto no encontrado.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void mostrarMensaje(JFrame frame, String mensaje) {
        mostrarMensaje(frame, mensaje, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void mostrarMensaje(JFrame frame, String mensaje, int tipo) {
        JOptionPane.showMessageDialog(frame, mensaje, "Mensaje", tipo);
    }
}
