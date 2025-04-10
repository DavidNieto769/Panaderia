package panaderia.persistencia;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchivoBinario {

    public static <T extends Serializable> void guardar(String ruta, List<T> objetos) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ruta))) {
            out.writeObject(objetos);
        } catch (IOException e) {
            System.err.println("Error al guardar en " + ruta + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> cargar(String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ruta))) {
            return (List<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar desde " + ruta + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
