package panaderia.vista;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;

import panaderia.vista.utilidadesvista.FiltroTexto;
import panaderia.modelo.*;
import panaderia.fabrica.*;

public class FormularioProducto {

    // Muestra un formulario para crear o editar un producto, devolviendo el objeto resultante
    public static Producto mostrarDialogo(Component parent, Producto productoExistente) throws Exception {
        String tipo = "Pan"; // Valor por defecto

        // Determina el tipo de producto según la acción: nuevo o edición
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

            if (tipo == null) return null; // Si se cancela el diálogo, retorna null
        } else {
            tipo = productoExistente instanceof Pan ? "Pan" : "Galleta";
        }

        // Campos del formulario con filtros de validación
        JTextField nombre = new JTextField(productoExistente != null ? productoExistente.getNombre() : "");
        ((AbstractDocument) nombre.getDocument()).setDocumentFilter(new FiltroTexto.SoloLetras());
        JTextField precio = new JTextField(productoExistente != null ? String.valueOf(productoExistente.getPrecioVenta()) : "");
        ((AbstractDocument) precio.getDocument()).setDocumentFilter(new FiltroTexto.SoloNumeros());
        JTextField costo = new JTextField(productoExistente != null ? String.valueOf(productoExistente.getCostoProduccion()) : "");
        ((AbstractDocument) costo.getDocument()).setDocumentFilter(new FiltroTexto.SoloNumeros());
        JTextField cantidad = new JTextField(productoExistente != null ? String.valueOf(productoExistente.getCantidad()) : "");
        ((AbstractDocument) cantidad.getDocument()).setDocumentFilter(new FiltroTexto.SoloNumeros());

        // Campo extra específico según el tipo de producto
        JCheckBox extra;
        if (tipo.equals("Pan")) {
            boolean tieneQueso = productoExistente instanceof Pan && ((Pan) productoExistente).isTieneQueso();
            extra = new JCheckBox("¿Tiene queso?", tieneQueso);
        } else {
            boolean tieneChispas = productoExistente instanceof Galleta && ((Galleta) productoExistente).isTieneChispas();
            extra = new JCheckBox("¿Tiene chispas de chocolate?", tieneChispas);
        }

        // Construcción del formulario
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

            // Se utiliza la fábrica adecuada para crear el producto
            Panaderia factory = tipo.equals("Pan") ? new PanFactory() : new GalletaFactory();
            return factory.hornear(nom, p, c, cant, conExtra);
        }

        return null; // Si se cancela, no se crea producto
    }
}
