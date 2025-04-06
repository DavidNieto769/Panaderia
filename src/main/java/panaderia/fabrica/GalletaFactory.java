package panaderia.fabrica;
import panaderia.modelo.*;

public class GalletaFactory implements Panaderia {

	public Producto hornear(String nombre, double precioVenta, double costoProduccion, int cantidad,
			boolean tieneChispas) {
		return new Galleta(nombre, precioVenta, costoProduccion, cantidad, tieneChispas);
	}   
}