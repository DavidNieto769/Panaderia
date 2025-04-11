package panaderia.controlador;

import java.awt.Component;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import panaderia.controlador.utilidades.ReporteVentasExporter;
import panaderia.modelo.*;
import panaderia.modelo.reporte.*;
import panaderia.dao.GalletaDAO;
import panaderia.dao.PanDAO;
import panaderia.persistencia.ArchivoBinario;
import panaderia.controlador.utilidades.ResultadoOperacion;
import panaderia.vista.VentaUI;

public class ControladorInventario {

    private Inventario inventario;
    private PanDAO panDAO;
    private GalletaDAO galletaDAO;

    public ControladorInventario() {
        this.inventario = new Inventario();
        this.panDAO = new PanDAO();
        this.galletaDAO = new GalletaDAO();

        inventario.getProductos().clear();

        List<Producto> productosCargados = new ArrayList<>();
        productosCargados.addAll(panDAO.obtenerTodos());
        productosCargados.addAll(galletaDAO.obtenerTodos());

        for (Producto p : productosCargados) {
            inventario.agregarProducto(p);
        }
    }

    public void agregarProducto(Producto producto) {
        inventario.agregarProducto(producto);

        if (producto instanceof Pan) {
            panDAO.insertar(producto, getSoloPanes());
        } else if (producto instanceof Galleta) {
            galletaDAO.insertar(producto, getSoloGalletas());
        }
    }

    public List<Producto> obtenerProductos() {
        return inventario.getProductos();
    }

    public List<Producto> filtrar(String nombre, String precioMax, String cantidadMin) {
        List<Producto> filtrados = inventario.getProductos();

        if (!nombre.isEmpty()) {
            filtrados = filtrados.stream()
                    .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (!precioMax.isEmpty()) {
            try {
                double max = Double.parseDouble(precioMax);
                filtrados = filtrados.stream()
                        .filter(p -> p.getPrecioVenta() <= max)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                // Error silencioso, podrías notificar a la vista si deseas
            }
        }

        if (!cantidadMin.isEmpty()) {
            try {
                int min = Integer.parseInt(cantidadMin);
                filtrados = filtrados.stream()
                        .filter(p -> p.getCantidad() >= min)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                // Error silencioso, podrías notificar a la vista si deseas
            }
        }

        return filtrados;
    }


    public void guardarSerializable(Producto producto) {
        if (producto instanceof Pan) {
            panDAO.insertar(producto, getSoloPanes());
        } else if (producto instanceof Galleta) {
            galletaDAO.insertar(producto, getSoloGalletas());
        }
    }

    public void editarProducto(Producto productoEditado) {
        if (productoEditado instanceof Pan) {
            panDAO.insertar(productoEditado, panDAO.obtenerTodos());
        } else if (productoEditado instanceof Galleta) {
            galletaDAO.insertar(productoEditado, galletaDAO.obtenerTodos());
        }
        sincronizarInventario(); // igual que en eliminar
    }



    public void eliminarProducto(Producto producto) {
        if (producto instanceof Pan) {
            panDAO.eliminar((Pan) producto);
        } else if (producto instanceof Galleta) {
            galletaDAO.eliminar((Galleta) producto);
        }
        sincronizarInventario(); // muy importante para reflejar los cambios
    }



    public void sincronizarInventario() {
        inventario.limpiar();
        inventario.getProductos().addAll(panDAO.obtenerTodos());
        inventario.getProductos().addAll(galletaDAO.obtenerTodos());
    }





    public void crearProducto(Component parentComponent, Runnable actualizarVista) {
        try {
            Producto producto = VentaUI.mostrarDialogoNuevo(parentComponent);
            if (producto != null) {
                agregarProducto(producto);
                guardarSerializable(producto);
                if (actualizarVista != null) actualizarVista.run();
            }
        } catch (Exception ex) {
            VentaUI.mostrarError(parentComponent, "Error al crear producto: " + ex.getMessage());
        }
    }

    public ResultadoOperacion registrarVenta(String nombreProducto, int cantidadVendida) {
        Producto producto = inventario.getProductos().stream()
                .filter(p -> p.getNombre().equals(nombreProducto))
                .findFirst()
                .orElse(null);

        if (producto == null) {
            return new ResultadoOperacion(false, "Producto no encontrado.");
        }

        if (producto.getCantidad() < cantidadVendida) {
            return new ResultadoOperacion(false, "No hay suficiente stock para realizar esta venta.");
        }

        producto.setCantidad(producto.getCantidad() - cantidadVendida);
        guardarSerializable(producto);

        Producto vendido = (producto instanceof Pan)
                ? new Pan(producto.getNombre(), producto.getPrecioVenta(), producto.getCostoProduccion(), cantidadVendida, producto.isExtra())
                : new Galleta(producto.getNombre(), producto.getPrecioVenta(), producto.getCostoProduccion(), cantidadVendida, producto.isExtra());

        Venta nuevaVenta = new Venta(new Date(), List.of(vendido));
        guardarVenta(nuevaVenta);

        return new ResultadoOperacion(true, "Venta realizada con éxito.");
    }

    public void guardarVenta(Venta venta) {
        List<Venta> ventas = ArchivoBinario.<Venta>cargar("reporteVentas.ser");
        ventas.add(venta);
        ArchivoBinario.guardar("reporteVentas.ser", ventas);
    }

    public void mostrarDialogoVenta(Component parentComponent, Runnable callbackActualizarTabla) {
        List<Producto> disponibles = inventario.getProductos().stream()
                .filter(p -> p.getCantidad() > 0)
                .toList();

        VentaUI.mostrar(parentComponent, disponibles, (nombreProducto, cantidadVendida) -> {
            ResultadoOperacion resultado = registrarVenta(nombreProducto, cantidadVendida);
            VentaUI.mostrarMensaje(parentComponent, resultado.getMensaje());
            if (resultado.isExito()) {
                callbackActualizarTabla.run();
            }
        });
    }

    public Producto obtenerProductoPorNombre(String nombre) {
        return inventario.buscarProductoPorNombre(nombre);
    }

    // Métodos auxiliares para filtrar por tipo
    private List<Producto> getSoloPanes() {
        return inventario.getProductos().stream()
                .filter(p -> p instanceof Pan)
                .collect(Collectors.toList());
    }

    private List<Producto> getSoloGalletas() {
        return inventario.getProductos().stream()
                .filter(p -> p instanceof Galleta)
                .collect(Collectors.toList());
    }
}
