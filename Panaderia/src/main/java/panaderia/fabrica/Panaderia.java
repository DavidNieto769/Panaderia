package panaderia.fabrica;

import panaderia.modelo.Producto;

// Esta interfaz representa una fábrica genérica en la panadería.
// Define el método 'hornear' que deben implementar las clases concretas (como fábricas de pan o galletas).
public interface Panaderia {
    // Método que se encarga de crear (hornear) un producto con los parámetros dados.
    // El parámetro 'extra' representa una característica adicional que puede variar según el tipo de producto.
    Producto hornear(String nombre, double precioVenta, double costoProduccion, int cantidad, boolean extra);
}
