package panaderia.vista;

import panaderia.controlador.ControladorInventario;
import panaderia.modelo.Producto;
import panaderia.vista.FormularioProducto;

import javax.swing.*;
import java.util.List;

public class ProductoUI {

    // Permite eliminar un producto seleccionado por el usuario
    public static void eliminarProducto(JFrame frame, ControladorInventario controlador, Runnable postAccion) {
        List<Producto> productos = controlador.obtenerProductos();

        if (productos.isEmpty()) {
            mostrarMensaje(frame, "No hay productos para eliminar.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Se crea un arreglo con los nombres de los productos disponibles
        String[] nombres = productos.stream().map(Producto::getNombre).toArray(String[]::new);

        // Muestra un diálogo para seleccionar el producto a eliminar
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
                postAccion.run(); // Ejecuta acción posterior (por ejemplo, actualizar vista)
            } else {
                mostrarMensaje(frame, "Producto no encontrado.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Permite editar un producto existente seleccionándolo primero
    public static void editarProducto(JFrame frame, ControladorInventario controlador, Runnable postAccion) {
        List<Producto> productos = controlador.obtenerProductos();

        if (productos.isEmpty()) {
            mostrarMensaje(frame, "No hay productos para editar.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Se crea un arreglo con los nombres de los productos disponibles
        String[] nombres = productos.stream().map(Producto::getNombre).toArray(String[]::new);

        // Muestra un diálogo para seleccionar el producto a editar
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
                    // Muestra el formulario con los datos actuales del producto
                    Producto editado = FormularioProducto.mostrarDialogo(frame, original);
                    if (editado != null) {
                        // Se reemplaza el producto anterior por el nuevo
                        controlador.eliminarProducto(original);
                        controlador.agregarProducto(editado);
                        mostrarMensaje(frame, "Producto editado correctamente.");
                        postAccion.run(); // Actualiza la vista
                    }
                } catch (Exception ex) {
                    mostrarMensaje(frame, "Error al editar producto: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                mostrarMensaje(frame, "Producto no encontrado.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método auxiliar para mostrar un mensaje informativo
    private static void mostrarMensaje(JFrame frame, String mensaje) {
        mostrarMensaje(frame, mensaje, JOptionPane.INFORMATION_MESSAGE);
    }

    // Método generalizado para mostrar mensajes con distintos tipos (info, error, etc.)
    private static void mostrarMensaje(JFrame frame, String mensaje, int tipo) {
        JOptionPane.showMessageDialog(frame, mensaje, "Mensaje", tipo);
    }
}
