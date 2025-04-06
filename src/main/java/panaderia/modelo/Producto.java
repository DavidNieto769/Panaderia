package panaderia.modelo;

public abstract class Producto{
  protected String nombre;
  protected double precioVenta;
  protected double costoProduccion;
  protected int cantidad;
  protected boolean extra;


  public Producto(String nombre, double precioVenta, double costoProduccion, int cantidad, boolean extra){
	  this.nombre = nombre;
    this.extra = extra;
    setPrecioVenta(precioVenta);
    setCostoProduccion(costoProduccion);
    setCantidad(cantidad);
  }

  public abstract String getDescripcion();

  //Getters

  public String getNombre(){
    return nombre;
  }
  public double getPrecioVenta(){
    return precioVenta;
  }
  public double getCostoProduccion(){
    return costoProduccion;
  }
  public int getCantidad(){
    return cantidad;
  }
  public boolean isExtra() {
      return extra;
  }

  //Setters
  public void setNombre(String nombre){
    this.nombre = nombre;
  }
 //Doble validación para que al registrar un producto, no importe el orden en el que se registren los datos.
  public void setPrecioVenta(double precioVenta){
    if(this.costoProduccion > 0 && precioVenta < this.costoProduccion){
      throw new IllegalArgumentException("El costo de produccion no puede ser mayor al precio de venta");
    }
    this.precioVenta = precioVenta;
  }

  public void setCostoProduccion(double costoProduccion){
    if(this.precioVenta > 0 && costoProduccion > this.precioVenta){
      throw new IllegalArgumentException("El costo de produccion no puede ser mayor al precio de venta");
    }
    this.costoProduccion = costoProduccion;
  }

  public void setCantidad(int cantidad){
    if (cantidad < 0) {
      throw new IllegalArgumentException("La cantidad no puede ser negativa.");
    }
    this.cantidad = cantidad;
  }
  public void setExtra(boolean extra){
    this.extra = extra;
  }

  
}