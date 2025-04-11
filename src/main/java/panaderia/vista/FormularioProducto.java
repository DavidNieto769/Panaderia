package panaderia.vista;

import javax.swing.*;
import java.awt.*;
import panaderia.modelo.*;
import panaderia.fabrica.*;

public class FormularioProducto {

    public static Producto mostrarDialogo(Component parent, Producto productoExistente) throws Exception {
        String tipo = "Pan"; // Valor por defecto

        // Preguntar el tipo solo si es un producto nuevo
        if (productoExistente == null) {
            String[] opciones = {"Pan", "Galleta"};
            tipo = (String) JOptionPane.showInputDialog(
                    parent,
                    "¿Qué tipo de producto desea crear?",
                    "Seleccionar Tipo",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (tipo == null) return null; // Cancelado
        } else {
            tipo = productoExistente instanceof Pan ? "Pan" : "Galleta";
        }

        JTextField nombre = new JTextField(productoExistente != null ? productoExistente.getNombre() : "");
        JTextField precio = new JTextField(productoExistente != null ? String.valueOf(productoExistente.getPrecioVenta()) : "");
        JTextField costo = new JTextField(productoExistente != null ? String.valueOf(productoExistente.getCostoProduccion()) : "");
        JTextField cantidad = new JTextField(productoExistente != null ? String.valueOf(productoExistente.getCantidad()) : "");

        JCheckBox extra;
        if (tipo.equals("Pan")) {
            boolean tieneQueso = productoExistente instanceof Pan && ((Pan) productoExistente).isTieneQueso();
            extra = new JCheckBox("¿Tiene queso?", tieneQueso);
        } else {
            boolean tieneChispas = productoExistente instanceof Galleta && ((Galleta) productoExistente).isTieneChispas();
            extra = new JCheckBox("¿Tiene chispas de chocolate?", tieneChispas);
        }

        Object[] campos = {
                "Nombre:", nombre,
                "Precio:", precio,
                "Costo:", costo,
                "Cantidad:", cantidad,
                extra
        };

        String titulo = productoExistente == null ? "Nuevo " + tipo : "Editar " + tipo;
        int opcion = JOptionPane.showConfirmDialog(parent, campos, titulo, JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {
            String nom = nombre.getText();
            double p = Double.parseDouble(precio.getText());
            double c = Double.parseDouble(costo.getText());
            int cant = Integer.parseInt(cantidad.getText());
            boolean conExtra = extra.isSelected();

            Panaderia factory = tipo.equals("Pan") ? new PanFactory() : new GalletaFactory();
            return factory.hornear(nom, p, c, cant, conExtra);
        }

        return null;
    }
}