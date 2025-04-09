package panaderia.vista;

import javax.swing.*;
import java.awt.*;
import panaderia.fabrica.*;
import panaderia.modelo.Producto;

public class FormularioProducto {

    public static Producto mostrarDialogo(Component parent) throws Exception {
        String[] opciones = { "Pan", "Galleta" };

        String tipo = (String) JOptionPane.showInputDialog(parent, "Tipo de producto", "Selecciona",
                JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        if (tipo == null) return null;

        JTextField nombre = new JTextField();
        JTextField precio = new JTextField();
        JTextField costo = new JTextField();
        JTextField cantidad = new JTextField();
        JCheckBox extra = new JCheckBox(tipo.equals("Pan") ? "¿Tiene queso?" : "¿Tiene chispas de chocolate?");

        Object[] campos = {
                "Nombre:", nombre,
                "Precio:", precio,
                "Costo:", costo,
                "Cantidad:", cantidad,
                extra
        };

        int opcion = JOptionPane.showConfirmDialog(parent, campos, "Nuevo " + tipo, JOptionPane.OK_CANCEL_OPTION);

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
