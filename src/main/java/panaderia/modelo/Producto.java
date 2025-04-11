package panaderia.modelo;

import java.io.Serializable;

// Clase abstracta que representa un producto genérico en la panadería
public abstract class Producto implements Serializable {

  private static final long serialVersionUID = 1L;
  protected String nombre;
  protected double precioVenta;
  protected double costoProduccion;
  protected int cantidad;
  protected boolean extra;

  // Constructor 
  public Producto(String nombre, double precioVenta, double costoProduccion, int cantidad, boolean extra) {
    this.nombre = nombre;
    this.extra = extra;

    setPrecioVenta(precioVenta);
    setCostoProduccion(costoProduccion);
    setCantidad(cantidad);
  }

  public abstract String getDescripcion();

  public String getNombre() {
    return nombre;
  }

  public double getPrecioVenta() {
    return precioVenta;
  }

  public double getCostoProduccion() {
    return costoProduccion;
  }

  public int getCantidad() {
    return cantidad;
  }

  public boolean isExtra() {
    return extra;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   * valida que el precio de venta no sea menor al costo de producción.
   * Esta validación se hace solo si el costo ya ha sido asignado previamente.
   */
  public void setPrecioVenta(double precioVenta) {
    if (this.costoProduccion > 0 && precioVenta < this.costoProduccion) {
      throw new IllegalArgumentException("El costo de producción no puede ser mayor al precio de venta");
    }
    this.precioVenta = precioVenta;
  }

  /**
   * Valida que el costo de producción no sea mayor al precio de venta.
   * Esta validación se hace solo si el precio ya ha sido asignado previamente.
   */
  public void setCostoProduccion(double costoProduccion) {
    if (this.precioVenta > 0 && costoProduccion > this.precioVenta) {
      throw new IllegalArgumentException("El costo de producción no puede ser mayor al precio de venta");
    }
    this.costoProduccion = costoProduccion;
  }

  // Valida que la cantidad no sea negativa
  public void setCantidad(int cantidad) {
    if (cantidad < 0) {
      throw new IllegalArgumentException("La cantidad no puede ser negativa.");
    }
    this.cantidad = cantidad;
  }

  public void setExtra(boolean extra) {
    this.extra = extra;
  }
}
