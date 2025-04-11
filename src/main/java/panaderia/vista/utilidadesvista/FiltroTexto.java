package panaderia.vista.utilidadesvista;

import javax.swing.*;
import javax.swing.text.*;

public class FiltroTexto {

    // Filtro para permitir solo letras en campos de texto
    public static class SoloLetras extends DocumentFilter {
    // Verifica si la cadena contiene solo letras
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null || string.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                super.insertString(fb, offset, string, attr);
            } else {
                mostrarAdvertencia("¡Advertencia! Solo se permiten letras en este campo.");
            }
        }

        @Override

        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
            // Reemplazo condicionado al ingreso de solo letras
            if (string == null || string.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                super.replace(fb, offset, length, string, attrs);
            } else {
                mostrarAdvertencia("¡Advertencia! Solo se permiten letras en este campo.");
            }
        }
    }


    // Filtro para permitir solo números en campos de texto
    public static class SoloNumeros extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
            } else {
                mostrarAdvertencia("¡Advertencia! Solo se permiten números en este campo.");
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
            if (string != null && string.matches("\\d*")) {
                super.replace(fb, offset, length, string, attrs);
            } else {
                mostrarAdvertencia("¡Advertencia! Solo se permiten números en este campo.");
            }
        }
    }

    //Muestra una advertencia en un cuadro de diálogo en el hilo de la interfaz gráfica
    private static void mostrarAdvertencia(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, mensaje, "Entrada no válida", JOptionPane.WARNING_MESSAGE);
        });
    }
}
