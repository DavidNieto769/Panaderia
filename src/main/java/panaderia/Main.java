package panaderia;

import javax.swing.SwingUtilities;
import panaderia.fabrica.*;
import panaderia.modelo.*;
import panaderia.vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        Panaderia panFactory = new PanFactory();
        Panaderia galletaFactory = new GalletaFactory();


        Producto pan1 = panFactory.hornear("Pan de queso", 3000, 1500, 20, true);
        Producto galleta1 = galletaFactory.hornear("Galleta dulce", 2000, 1000, 15, true);

        System.out.println("=== Productos creados ===");
        System.out.println("Pan: " + pan1.getDescripcion());
        System.out.println("Precio de venta: $" + pan1.getPrecioVenta());
        System.out.println("Costo de producción: $" + pan1.getCostoProduccion());
        System.out.println("Cantidad: " + pan1.getCantidad());
        System.out.println();

        System.out.println("Galleta: " + galleta1.getDescripcion());
        System.out.println("Precio de venta: $" + galleta1.getPrecioVenta());
        System.out.println("Costo de producción: $" + galleta1.getCostoProduccion());
        System.out.println("Cantidad: " + galleta1.getCantidad());
        VentanaPrincipal v = new VentanaPrincipal();
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}

/*

import javax.swing.SwingUtilities;
import panaderia.vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
*/