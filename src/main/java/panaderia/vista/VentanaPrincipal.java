package panaderia.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import panaderia.controlador.*;
import panaderia.modelo.*;
import panaderia.modelo.reporte.Venta;
import panaderia.modelo.reporte.VentasSerializable;

import java.awt.*;
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

        btnVender.addActionListener(e -> {
            controlador.mostrarDialogoVenta(this, () -> actualizarTabla(controlador.obtenerProductos()));
        });

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

    private void mostrarVentas() {
        DefaultTableModel modeloVentas = new DefaultTableModel(new Object[]{
                "Fecha", "Producto", "Cantidad", "Precio Unitario", "Total"
        }, 0);

        List<Venta> ventas = VentasSerializable.cargarVentas();
        for (Venta v : ventas) {
            for (Producto p : v.getProductos()) {
                Object[] fila = new Object[]{
                        v.getFecha(),
                        p.getNombre(),
                        p.getCantidad(),
                        p.getPrecioVenta(),
                        p.getCantidad() * p.getPrecioVenta()
                };
                modeloVentas.addRow(fila);
            }
        }

        if (modeloVentas.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay ventas registradas.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTable tablaVentas = new JTable(modeloVentas);
        JScrollPane scrollPane = new JScrollPane(tablaVentas);

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
