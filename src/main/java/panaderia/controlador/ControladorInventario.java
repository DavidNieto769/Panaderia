package panaderia.controlador;

import java.awt.Component;
import java.util.*;
import java.util.stream.Collectors;

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
        // Carga los productos desde archivos binarios al iniciar el sistema

        inventario.obtenerProductos().clear();

        List<Producto> productosCargados = new ArrayList<>();
        productosCargados.addAll(panDAO.obtenerTodos());
        productosCargados.addAll(galletaDAO.obtenerTodos());

        for (Producto p : productosCargados) {
            inventario.agregarProducto(p);
        }
    }

    public void agregarProducto(Producto producto) {
        inventario.agregarProducto(producto);
        // Guarda el producto en su archivo correspondiente
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
        List<Producto> filtrados = inventario.obtenerProductos();

        if (!nombre.isEmpty()) {
            filtrados = filtrados.stream()
                    .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .collect(Collectors.toList());
        }
        // Filtra por nombre si no está vacío
        if (!precioMax.isEmpty()) {
            try {
                double max = Double.parseDouble(precioMax);
                filtrados = filtrados.stream()
                        .filter(p -> p.getPrecioVenta() <= max)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                // Error silencioso"
            }
        }
        // Filtra por cantidad mínima, si es válida
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

    // Guarda el producto en archivo .ser según su tipo
    public void guardarSerializable(Producto producto) {
        if (producto instanceof Pan) {
            panDAO.insertar(producto, getSoloPanes());
        } else if (producto instanceof Galleta) {
            galletaDAO.insertar(producto, getSoloGalletas());
        }
    }

    public void eliminarProducto(Producto producto) {
        if (producto instanceof Pan) {
            panDAO.eliminar((Pan) producto);
        } else if (producto instanceof Galleta) {
            galletaDAO.eliminar((Galleta) producto);
        }
        sincronizarInventario(); // Refresca los datos en memoria tras eliminar
    }

    //inventario desde archivos serializados
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
            VentaUI.mostrarError(parentComponent, "Error al crear producto: Debes llenar todos los espacios ");
        }
    }

// Registra una venta y actualiza inventario y archivo de ventas

    public ResultadoOperacion registrarVenta(String nombreProducto, int cantidadVendida) {
        Producto producto = inventario.obtenerProductos().stream()
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

        return new ResultadoOperacion(true);
    }

    public void guardarVenta(Venta venta) {
        // Agrega una venta nueva al archivo de ventas serializadas
        List<Venta> ventas = ArchivoBinario.<Venta>cargar("reporteVentas.ser");
        ventas.add(venta);
        ArchivoBinario.guardar("reporteVentas.ser", ventas);
    }

    public void mostrarDialogoVenta(Component parentComponent, Runnable callbackActualizarTabla) {
        List<Producto> disponibles = inventario.obtenerProductos().stream()
                .filter(p -> p.getCantidad() > 0)
                .toList();

        VentaUI.mostrar(parentComponent, disponibles, (nombreProducto, cantidadVendida) -> {
            ResultadoOperacion resultado = registrarVenta(nombreProducto, cantidadVendida);
            //VentaUI.mostrarMensaje(parentComponent, resultado.getMensaje());
            if (resultado.isExito()) {
                callbackActualizarTabla.run();
            }
        });
    }

    public Producto obtenerProductoPorNombre(String nombre) {
        return inventario.buscarProductoPorNombre(nombre);
    }

    // Métodos auxiliares para filtrar los productos según su tipo
    private List<Producto> getSoloPanes() {
        return inventario.obtenerProductos().stream()
                .filter(p -> p instanceof Pan)
                .collect(Collectors.toList());
    }

    private List<Producto> getSoloGalletas() {
        return inventario.obtenerProductos().stream()
                .filter(p -> p instanceof Galleta)
                .collect(Collectors.toList());
    }
}
