package panaderia;

import panaderia.vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        // Asegúrate de ejecutar en el hilo de eventos de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
