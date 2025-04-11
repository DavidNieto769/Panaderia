package panaderia;

import panaderia.vista.VentanaPrincipal;

public class Main {
    //Llamando la ventana principal donde se realizan la mayoría de operaciones
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
