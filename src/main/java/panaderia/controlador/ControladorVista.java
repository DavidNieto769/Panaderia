package panaderia.controlador;

import panaderia.modelo.Producto;
import panaderia.modelo.reporte.Venta;
import panaderia.modelo.reporte.VentasSerializable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        mostrarMensaje(frame, "Reporte generado exitosamente.");
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
            mostrarMensaje(frame, "No hay ventas registradas.", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTable tablaVentas = new JTable(modeloVentas);
        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        JOptionPane.showMessageDialog(frame, scrollPane, "Ventas realizadas", JOptionPane.INFORMATION_MESSAGE);
    }

    public void aplicarFiltros(String nombre, String precioTexto, String cantidadTexto, Consumer<List<Producto>> callback, Consumer<String> onError) {
        nombre = nombre.trim();
        precioTexto = precioTexto.trim();
        cantidadTexto = cantidadTexto.trim();

        if (!precioTexto.isEmpty() && !esNumero(precioTexto)) {
            onError.accept("El precio debe ser un número válido.");
            return;
        }

        if (!cantidadTexto.isEmpty() && !esEntero(cantidadTexto)) {
            onError.accept("La cantidad debe ser un número entero.");
            return;
        }

        List<Producto> filtrados = controlador.filtrar(nombre, precioTexto, cantidadTexto);
        callback.accept(filtrados);
    }

    public List<Producto> obtenerProductos() {
        return controlador.obtenerProductos();
    }

    private boolean esNumero(String texto) {
        try {
            Double.parseDouble(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean esEntero(String texto) {
        try {
            Integer.parseInt(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void mostrarMensaje(JFrame frame, String mensaje) {
        mostrarMensaje(frame, mensaje, JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMensaje(JFrame frame, String mensaje, int tipo) {
        JOptionPane.showMessageDialog(frame, mensaje, "Información", tipo);
    }

    public static JButton crearBotonRedondeado(String texto, String tooltip) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isArmed() && getModel().isPressed()) {
                    g2.setColor(new Color(100, 200, 150)); // verde suave
                } else {
                    g2.setColor(new Color(60, 63, 65)); // fondo normal
                }

                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 14, 14);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        boton.setToolTipText(tooltip);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setOpaque(false);
        boton.setForeground(new Color(240, 240, 240));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13)); // más chico
        boton.setMargin(new Insets(6, 10, 6, 10)); // menos espacio
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }


}
