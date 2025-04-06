package panaderia.fabrica;
import panaderia.modelo.Producto;


public interface Panaderia {
    Producto hornear(String nombre, double precioVenta, double costoProduccion, int cantidad, boolean extra);
}