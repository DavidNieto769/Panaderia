package panaderia.fabrica;

import panaderia.modelo.Galleta;
import panaderia.modelo.Producto;

public class GalletaFactory implements Panaderia {
    @Override
    public Producto hornear(String nombre, double precioVenta, double costoProduccion, int cantidad, boolean tieneChispas) {
        // Crea y devuelve una nueva instancia de Galleta con los parámetros especificados.
        return new Galleta(nombre, precioVenta, costoProduccion, cantidad, tieneChispas);
    }
}
