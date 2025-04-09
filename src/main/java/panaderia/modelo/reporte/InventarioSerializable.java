package panaderia.modelo.reporte;

import panaderia.modelo.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.*;
import java.util.*;

public class InventarioSerializable {

    // Método estático que guarda una lista de productos en un archivo CSV
    public static void guardarProductosSerializable(List<Producto> productos) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("productos.ser"))) {
            out.writeObject(productos);
            System.out.println("Productos guardados con éxito en productos.ser");

        } catch (IOException e) {
            // En caso de error al guardar el archivo, se imprime la traza
            e.printStackTrace();
        }
    }

    // Método estático que carga una lista de productos desde un archivo
    public static List<Producto> cargarProductosSerializable() {

        ArrayList<Producto> productosCargados = null;
        File file = new File("productos.ser");
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("productos.ser"))) {
            return (ArrayList<Producto>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }
}
