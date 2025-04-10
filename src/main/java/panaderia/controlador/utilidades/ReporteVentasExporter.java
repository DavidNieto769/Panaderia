package panaderia.controlador.utilidades;


import panaderia.modelo.reporte.Venta;
import panaderia.modelo.Producto;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReporteVentasExporter {

    public static boolean guardarComoCSV(List<Venta> ventas) {
        String fecha = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String nombreCSV = "reporteVentas-" + fecha + ".csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreCSV))) {
            writer.println("Fecha,Producto,Cantidad,Precio Unitario,Total");

            for (Venta v : ventas) {
                for (Producto p : v.getProductos()) {
                    double total = p.getCantidad() * p.getPrecioVenta();
                    writer.printf("%s,%s,%d,%.2f,%.2f%n",
                            v.getFecha(),
                            p.getNombre(),
                            p.getCantidad(),
                            p.getPrecioVenta(),
                            total
                    );
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
