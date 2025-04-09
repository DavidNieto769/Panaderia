package panaderia.vista;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import panaderia.controlador.*;
import panaderia.fabrica.*;
import panaderia.modelo.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private ControladorInventario controlador;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField filtroNombre, filtroPrecio, filtroCantidad;

    public VentanaPrincipal() {
        controlador = new ControladorInventario();

        setTitle("Gestión de Panadería");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(new Color(106, 236, 186));
        initComponents();
        actualizarTabla(controlador.obtenerProductos());
    }

    private void initComponents() {
        // Se crea el panel principal con un BorderLayout

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelLogo = new JPanel(new BorderLayout());
        panelLogo.setSize(60,60);
        // Cargar imagen original
        ImageIcon iconoOriginal = new ImageIcon("C:\\Users\\daredevil769\\Downloads\\Panaderia\\src\\main\\java\\panaderia\\vista\\imgs\\bread.png");
        // Escalar imagen
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(panelLogo.getWidth(), panelLogo.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);
        JLabel logo = new JLabel(iconoEscalado);
        logo.setHorizontalAlignment(JLabel.CENTER);
        panelLogo.add(logo);
        this.add(panelLogo);
        // Se configura la tabla que mostrará los productos
        modeloTabla = new DefaultTableModel(new Object[] { "Nombre", "Precio", "Costo", "Cantidad", "Extra" }, 0);
        tabla = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tabla); // Se añade la tabla dentro de un JScrollPane para hacerla desplazable

        // Panel superior que contiene los botones y filtros
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1)); // Dividido en dos filas

        // Panel con los botones de acciones
        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnLimpiar = new JButton("Limpiar Filtros");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnVender = new JButton("Vender producto");
        JButton btnVerVentas = new JButton("Ver ventas");

        // Se asignan las acciones a los botones con expresiones lambda
        btnAgregar.addActionListener(e ->
                controlador.crearProducto(this, () -> actualizarTabla(controlador.obtenerProductos()))
        );

        btnVender.addActionListener(e -> mostrarDialogoVenta());
        btnVerVentas.addActionListener(e -> mostrarVentas());
        btnFiltrar.addActionListener(e -> filtrar());
        btnLimpiar.addActionListener(e -> {
            // Limpia los campos de filtro y actualiza la tabla con todos los productos
            filtroNombre.setText("");
            filtroPrecio.setText("");
            filtroCantidad.setText("");
            actualizarTabla(controlador.obtenerProductos());
        });
        btnGuardar.addActionListener(e -> {
            // Guarda el reporte del inventario y de ventas con fecha
            controlador.guardarReporteConFecha();
            controlador.guardarReporteVentasConFecha();
            JOptionPane.showMessageDialog(this, "Reporte generado exitosamente.");
        });

        // Se agregan los botones al panel de botones
        panelBotones.add(btnAgregar);
        panelBotones.add(btnFiltrar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnVender);
        panelBotones.add(btnVerVentas);

        // Panel con los campos de filtro
        JPanel panelFiltros = new JPanel();
        filtroNombre = new JTextField(10);
        filtroPrecio = new JTextField(5);
        filtroCantidad = new JTextField(5);
        panelFiltros.add(new JLabel("Nombre:"));
        panelFiltros.add(filtroNombre);
        panelFiltros.add(new JLabel("Precio máximo:"));
        panelFiltros.add(filtroPrecio);
        panelFiltros.add(new JLabel("Cantidad mínima:"));
        panelFiltros.add(filtroCantidad);

        // Se agregan los subpaneles al panel superior
        panelSuperior.add(panelBotones);
        panelSuperior.add(panelFiltros);

        // Se agregan los componentes al panel principal
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        add(panelPrincipal); // Se añade el panel principal al contenedor
    }


    private void mostrarDialogoVenta() {
        // Se obtiene la lista de productos disponibles (con cantidad mayor a 0)
        List<Producto> disponibles = controlador.obtenerProductos().stream()
            .filter(p -> p.getCantidad() > 0)
            .toList();

        // Si no hay productos disponibles, se muestra un mensaje al usuario y se sale del método
        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos disponibles para vender.");
            return;
        }

        // Se crea un combo box (lista desplegable) para seleccionar el producto
        JComboBox<String> combo = new JComboBox<>();
        for (Producto p : disponibles) {
            combo.addItem(p.getNombre()); // Se agregan los nombres de los productos disponibles
        }

        // Campo de texto para que el usuario ingrese la cantidad a vender
        JTextField campoCantidad = new JTextField();

        // Se crea un panel para organizar los componentes del diálogo
        JPanel panelVenta = new JPanel(new GridLayout(2, 2));
        panelVenta.add(new JLabel("Producto:"));
        panelVenta.add(combo);
        panelVenta.add(new JLabel("Cantidad a vender:"));
        panelVenta.add(campoCantidad);

        // Se muestra un cuadro de diálogo con el panel de venta
        int opcion = JOptionPane.showConfirmDialog(
            this,
            panelVenta,
            "Venta de producto",
            JOptionPane.OK_CANCEL_OPTION
        );

        // Si el usuario presiona OK, se procede a procesar la venta
        if (opcion == JOptionPane.OK_OPTION) {
            String productoSeleccionado = (String) combo.getSelectedItem(); // Nombre del producto seleccionado
            String cantidadTexto = campoCantidad.getText().trim(); // Cantidad ingresada por el usuario

            try {
                int cantidad = Integer.parseInt(cantidadTexto); // Se convierte a entero
                if (cantidad <= 0) {
                    throw new NumberFormatException("La cantidad debe ser mayor que cero.");
                }

                // Se registra la venta del producto con la cantidad especificada
                registrarVentaProducto(productoSeleccionado, cantidad);

            } catch (NumberFormatException e) {
                // Si la cantidad ingresada no es válida, se muestra un mensaje de error
                JOptionPane.showMessageDialog(this, "Cantidad inválida: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void registrarVentaProducto(String nombreProducto, int cantidadVendida) {
        // Se obtiene la lista actual de productos desde el controlador
        List<Producto> productos = controlador.obtenerProductos();

        // Se busca el producto cuyo nombre coincida con el seleccionado
        Producto producto = productos.stream()
            .filter(p -> p.getNombre().equals(nombreProducto))
            .findFirst()
            .orElse(null);

        // Si el producto no se encuentra, se muestra un mensaje de error y se termina el proceso
        if (producto == null) {
            JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si la cantidad en inventario es menor a la solicitada para la venta, se avisa al usuario
        if (producto.getCantidad() < cantidadVendida) {
            JOptionPane.showMessageDialog(this, "No hay suficiente stock para realizar esta venta.", "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Se descuenta la cantidad vendida del inventario del producto
        producto.setCantidad(producto.getCantidad() - cantidadVendida);

        // Se guarda el nuevo estado del inventario en el archivo serializado
        controlador.guardarSerializable();

        // Se obtiene la fecha y hora actual para registrar la venta
        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Se calcula el total de la venta (cantidad vendida * precio unitario)
        double total = cantidadVendida * producto.getPrecioVenta();

        // Se forma la línea de texto que representa la venta
        String lineaVenta = String.format("%s,%s,%d,%.2f,%.2f\n", fecha, nombreProducto, cantidadVendida, producto.getPrecioVenta(), total);

        // Se escribe la línea de venta al final del archivo "reporteVentas.ser"


        try (FileWriter fw = new FileWriter("reporteVentas.ser", true)) {
            fw.write(lineaVenta);
        } catch (IOException e) {
            // Si ocurre un error al guardar, se informa al usuario
            JOptionPane.showMessageDialog(this, "Error al guardar la venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Se actualiza la tabla en la interfaz gráfica con los productos actualizados
        actualizarTabla(productos);

        // Se notifica al usuario que la venta fue realizada con éxito
        JOptionPane.showMessageDialog(this, "Venta realizada con éxito.");
    }



    private void mostrarVentas() {
        // Se crea un modelo de tabla con los nombres de las columnas para mostrar las ventas
        DefaultTableModel modeloVentas = new DefaultTableModel(new Object[] {
            "Fecha", "Producto", "Cantidad", "Precio Unitario", "Total"
        }, 0);

        // Se intenta abrir y leer el archivo  que contiene las ventas
        try (BufferedReader br = new BufferedReader(new FileReader("reporteVentas.ser"))) {
            String linea;
            // Se lee línea por línea del archivo
            while ((linea = br.readLine()) != null) {
                // Se separa cada línea en columnas usando la coma como delimitador
                String[] datos = linea.split(",");
                // Si la línea tiene los 5 campos esperados, se añade al modelo de la tabla
                if (datos.length == 5) {
                    modeloVentas.addRow(datos);
                }
            }

        } catch (IOException e) {
            // Si ocurre un error al leer el archivo, se muestra un mensaje de error
            JOptionPane.showMessageDialog(this, "No se pudo leer el archivo de ventas.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Se crea una tabla visual con el modelo de datos cargado
        JTable tablaVentas = new JTable(modeloVentas);
        JScrollPane scrollPane = new JScrollPane(tablaVentas);

        // Se muestra la tabla dentro de un cuadro de diálogo informativo
        JOptionPane.showMessageDialog(this, scrollPane, "Ventas realizadas", JOptionPane.INFORMATION_MESSAGE);
    }



    
    private void filtrar() {
        // Se obtienen los textos ingresados por el usuario para filtrar por nombre, precio y cantidad
        String nombreFiltro = filtroNombre.getText().trim();
        String precioTexto = filtroPrecio.getText().trim();
        String cantidadTexto = filtroCantidad.getText().trim();

        // Se llama al controlador para obtener la lista filtrada de productos
        List<Producto> filtrados = controlador.filtrar(nombreFiltro, precioTexto, cantidadTexto);

        // Se actualiza la tabla para mostrar solo los productos que cumplen con los filtros
        actualizarTabla(filtrados);
    }

    private void actualizarTabla(List<Producto> lista) {
        // Se limpia completamente la tabla eliminando todas las filas actuales
        modeloTabla.setRowCount(0); 

        // Por cada producto en la lista recibida, se agrega una nueva fila con sus datos
        for (Producto p : lista) {
            modeloTabla.addRow(new Object[] {
                    p.getNombre(),
                    p.getPrecioVenta(),
                    p.getCostoProduccion(),
                    p.getCantidad(),
                    // Se muestra "Sí" o "No" dependiendo del tipo de producto y si tiene extra
                    p instanceof Pan ? (((Pan) p).isTieneQueso() ? "Sí" : "No")
                            : (((Galleta) p).isTieneChispas() ? "Sí" : "No")
            });
        }
    }

}
