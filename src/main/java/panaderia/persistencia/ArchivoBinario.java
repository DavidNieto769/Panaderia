package panaderia.persistencia;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchivoBinario {

    // Método genérico que guarda una lista de objetos serializables en un archivo binario
    public static <T extends Serializable> void guardar(String ruta, List<T> objetos) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ruta))) {
            out.writeObject(objetos); // Escribe toda la lista en el archivo
        } catch (IOException e) {
            System.err.println("Error al guardar en " + ruta + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> cargar(String ruta) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return new ArrayList<>(); // Si el archivo no existe, retorna lista vacía
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ruta))) {
            return (List<T>) in.readObject(); // Lee y convierte el contenido a lista del tipo indicado
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar desde " + ruta + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // En caso de error, devuelve lista vacía para evitar interrupciones
        }
    }
}
