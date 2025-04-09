package panaderia.utilidades;

import javax.swing.*;
import javax.swing.text.*;

public class FiltroSoloLetras extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null || !contieneNumero(string)) {
            super.insertString(fb, offset, string, attr);
        } else {
            mostrarAdvertencia();
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
        if (string == null || !contieneNumero(string)) {
            super.replace(fb, offset, length, string, attrs);
        } else {
            mostrarAdvertencia();
        }
    }

    private boolean contieneNumero(String texto) {
        return texto.matches(".*\\d.*"); // Verifica si el texto contiene un número
    }

    private void mostrarAdvertencia() {
        // Usar SwingUtilities para asegurarse de que el JOptionPane se ejecute en el hilo correcto
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "¡Advertencia! Solo se permiten letras en el campo 'Nombre'.",
                    "Entrada no válida", JOptionPane.WARNING_MESSAGE);
        });
    }
}
