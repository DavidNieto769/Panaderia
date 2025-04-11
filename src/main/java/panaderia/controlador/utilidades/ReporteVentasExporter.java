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

    // Permite generar el reporte de ventas, tomando el .ser y convirtiéndolo a CSV después de des-serializar
    public static boolean guardarComoCSV(List<Venta> ventas) {
        String fecha = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()); // Nombre único para el archivo
        String nombreCSV = "reporteVentas-" + fecha + ".csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreCSV))) {
            writer.println("Fecha,Producto,Cantidad,Precio Unitario,Total"); // Encabezado CSV

            for (Venta v : ventas) {
                for (Producto p : v.getProductos()) {
                    double total = p.getCantidad() * p.getPrecioVenta(); // Cálculo del total por producto en venta
                    writer.printf("%s,%s,%d,%.2f,%.2f%n",
                            v.getFecha(),
                            p.getNombre(),
                            p.getCantidad(),
                            p.getPrecioVenta(),
                            total
                    );
                }
            }

            return true; // Operación exitosa
        } catch (IOException e) {
            e.printStackTrace(); // Imprime errores en consola si ocurre un problema al escribir
            return false;
        }
    }
}
