package panaderia.modelo.reporte;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VentasSerializable {
    private static final String ARCHIVO = "reporteVentas.ser";

    public static void guardarVenta(Venta venta) {
        List<Venta> ventas = cargarVentas();
        ventas.add(venta);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            out.writeObject(ventas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Venta> cargarVentas() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            return (List<Venta>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}