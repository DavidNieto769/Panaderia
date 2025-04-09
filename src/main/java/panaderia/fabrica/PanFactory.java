package panaderia.fabrica;
import panaderia.modelo.Pan;
import panaderia.modelo.Producto;

public class PanFactory implements Panaderia {
    @Override
    public Producto hornear(String nombre, double precioVenta, double costoProduccion, int cantidad, boolean tieneQueso) {
        return new Pan(nombre, precioVenta, costoProduccion, cantidad, tieneQueso);
    }
}
