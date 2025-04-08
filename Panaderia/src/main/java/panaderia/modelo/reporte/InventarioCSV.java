package panaderia.modelo.reporte;

import panaderia.modelo.*;

import java.io.*;
import java.util.*;

public class InventarioCSV {

    // Método estático que guarda una lista de productos en un archivo CSV
    public static void guardarProductosCSV(List<Producto> productos, String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            // Se escribe la cabecera del archivo
            writer.append("Nombre,Precio,Costo,Cantidad,Extra,Tipo\n");

            // Se recorre la lista de productos para escribir cada uno en una línea del archivo
            for (Producto p : productos) {
                String extra = "";
                String tipo = "";

                // Se determina el tipo de producto y su característica extra
                if (p instanceof Pan) {
                    extra = ((Pan) p).isTieneQueso() ? "Queso" : "Sin Queso";
                    tipo = "Pan";
                } else if (p instanceof Galleta) {
                    extra = ((Galleta) p).isTieneChispas() ? "Chispas" : "Sin Chispas";
                    tipo = "Galleta";
                }

                // Se escribe la línea correspondiente al producto en el archivo
                writer.append(String.format("%s,%.2f,%.2f,%d,%s,%s\n",
                        p.getNombre(), p.getPrecioVenta(), p.getCostoProduccion(),
                        p.getCantidad(), extra, tipo));
            }
        } catch (IOException e) {
            // En caso de error al guardar el archivo, se imprime la traza
            e.printStackTrace();
        }
    }

    // Método estático que carga una lista de productos desde un archivo CSV
    public static List<Producto> cargarProductosCSV(String rutaArchivo) {
        List<Producto> productos = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea = reader.readLine(); // Se lee la cabecera

            // Se leen las líneas restantes del archivo, una por una
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");

                // Se omite la línea si no contiene exactamente 6 elementos
                if (partes.length != 6) continue;

                // Se extraen los campos del producto
                String nombre = partes[0];
                double precio = Double.parseDouble(partes[1]);
                double costo = Double.parseDouble(partes[2]);
                int cantidad = Integer.parseInt(partes[3]);
                String extra = partes[4];
                String tipo = partes[5];

                // Se crea el objeto correspondiente según el tipo y se añade a la lista
                if (tipo.equals("Pan")) {
                    boolean tieneQueso = extra.equalsIgnoreCase("Queso");
                    productos.add(new Pan(nombre, precio, costo, cantidad, tieneQueso));
                } else if (tipo.equals("Galleta")) {
                    boolean tieneChispas = extra.equalsIgnoreCase("Chispas");
                    productos.add(new Galleta(nombre, precio, costo, cantidad, tieneChispas));
                }
            }
        } catch (IOException e) {
            // En caso de error al leer el archivo, se muestra un mensaje en consola
            System.out.println("Error al leer el CSV: " + e.getMessage());
        }

        return productos;
    }
}
