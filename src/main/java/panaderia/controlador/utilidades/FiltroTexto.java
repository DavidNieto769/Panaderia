package panaderia.controlador.utilidades;

import javax.swing.*;
import javax.swing.text.*;

public class FiltroTexto {

    // Filtro para solo letras
    public static class SoloLetras extends DocumentFilter {

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
            if (string == null || string.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]*")) {
                super.replace(fb, offset, length, string, attrs);
            } else {
                mostrarAdvertencia("¡Advertencia! Solo se permiten letras en este campo.");
            }
        }
    }

    // Filtro para solo números
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

    // Método compartido para mostrar advertencias
    private static void mostrarAdvertencia(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, mensaje, "Entrada no válida", JOptionPane.WARNING_MESSAGE);
        });
    }
}
