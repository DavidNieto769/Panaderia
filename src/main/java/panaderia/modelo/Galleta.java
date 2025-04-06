package panaderia.modelo;

public class Galleta extends Producto{
  private boolean tieneChispas;


  public Galleta(String nombre, double precioVenta, double costoProduccion, int cantidad, boolean tieneChispas){
    super(nombre, precioVenta, costoProduccion, cantidad, tieneChispas);
    this.tieneChispas = tieneChispas;
  }

  @Override
  public String getDescripcion() {
      return nombre + " (Galleta)" + (extra ? " con chispas de chocolate" : "");
  }


  public boolean isTieneChispas(){
    return tieneChispas;
  }

  public void setTieneChispas(boolean tieneChispas){
    this.tieneChispas = tieneChispas;
  }

}