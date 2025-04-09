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

/* import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.*;
import com.formdev.flatlaf.ui.FlatTableUI;
import panaderia.vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {

        try{
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);  // Salimos del programa con código de error 1.
        }
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
*/



