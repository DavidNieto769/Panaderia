package panaderia.dao;

import panaderia.modelo.reporte.Venta;
import panaderia.persistencia.ArchivoBinario;

import java.util.List;

public class VentaDAO {

    private final String archivoVentas = "reporteVentas.ser";

    public List<Venta> obtenerVentas() {
        return ArchivoBinario.cargar(archivoVentas);
    }

    public void guardarVentas(List<Venta> ventas) {
       // ArchivoBinario.guardar(ventas, archivoVentas);
    }
}
