package panaderia.modelo.reporte;

import panaderia.modelo.Producto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VentasSerializable {
    // Método estático que guarda una lista de productos en un archivo CSV
    public static void guardarVentasSerializable(List<Producto> productos) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ventas.ser"))) {
            out.writeObject(productos);
            System.out.println("Productos guardados con éxito en productos.ser");

        } catch (IOException e) {
            // En caso de error al guardar el archivo, se imprime la traza
            e.printStackTrace();
        }
    }

    // Método estático que carga una lista de productos desde un archivo CSV
    public static List<Producto> cargarProductosSerializable() {

        ArrayList<Producto> productosCargados = null;
        File file = new File("ventas.ser");
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("ventas.ser"))) {
            return (ArrayList<Producto>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }
}
