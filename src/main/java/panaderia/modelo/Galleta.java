package panaderia.modelo;

import java.io.Serializable;

// La clase Galleta extiende de Producto e incorpora un atributo adicional: tieneChispas
public class Galleta extends Producto implements Serializable {

  // Atributo que indica si la galleta tiene chispas de chocolate
  private boolean tieneChispas;

  // Constructor que recibe los parámetros básicos más si tiene chispas
  public Galleta(String nombre, double precioVenta, double costoProduccion, int cantidad, boolean tieneChispas) {
    // Se llama al constructor de la clase padre (Producto), pasando también el valor de "extra"
    super(nombre, precioVenta, costoProduccion, cantidad, tieneChispas);
    this.tieneChispas = tieneChispas;
  }

  // Implementación del método abstracto heredado, que retorna una descripción textual del producto
  @Override
  public String getDescripcion() {
    // Devuelve una descripción indicando el nombre, tipo de producto y si tiene chispas de chocolate
    return nombre + " (Galleta)" + (extra ? " con chispas de chocolate" : "");
  }

  // Método getter para saber si la galleta tiene chispas
  public boolean isTieneChispas() {
    return tieneChispas;
  }

  // Método setter para modificar si la galleta tiene chispas
  public void setTieneChispas(boolean tieneChispas) {
    this.tieneChispas = tieneChispas;
  }

}
