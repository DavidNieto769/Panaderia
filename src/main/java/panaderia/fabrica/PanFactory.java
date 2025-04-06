package panaderia.fabrica;
import panaderia.modelo.*;

public class PanFactory implements Panaderia {

	public Producto hornear(String nombre, double precioVenta, double costoProduccion, int cantidad,
			boolean tieneQueso) {
		return new Pan(nombre, precioVenta, costoProduccion, cantidad, tieneQueso);
	}


}
