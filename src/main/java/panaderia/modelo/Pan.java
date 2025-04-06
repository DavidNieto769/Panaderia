package panaderia.modelo;


public class Pan extends Producto{
  private boolean tieneQueso;


  public Pan(String nombre, double precioVenta, double costoProduccion, int cantidad, boolean tieneQueso){
    super(nombre, precioVenta, costoProduccion, cantidad, tieneQueso);
    this.tieneQueso = tieneQueso;
  }

  @Override
  public String getDescripcion() {
      return nombre + " (Pan)" + (extra ? " con queso" : "");
  }


  public boolean isTieneQueso(){
    return tieneQueso;
  }

  public void setTieneQueso(boolean tieneQueso){
    this.tieneQueso = tieneQueso;
  }
  
}