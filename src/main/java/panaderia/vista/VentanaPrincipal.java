package panaderia.vista;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.util.List;

import panaderia.controlador.ControladorInventario;
import panaderia.controlador.ControladorVista;
import panaderia.vista.utilidadesvista.FiltroTexto;
import panaderia.modelo.Producto;


public class VentanaPrincipal extends JFrame {
    private final ControladorVista controladorVista;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField filtroNombre, filtroPrecio, filtroCantidad;

    public VentanaPrincipal() {
        ControladorInventario controlador = new ControladorInventario();
        controladorVista = new ControladorVista(controlador);

        setTitle("Gestión de Panadería UD");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage("icono_panaderia.png"));

        initComponents();
        actualizarTabla(controladorVista.obtenerProductos());
        configurarFiltrosDinamicos();
    }

    private void initComponents() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());

        panelPrincipal.add(crearPanelSuperior(), BorderLayout.NORTH);
        panelPrincipal.add(crearTablaScroll(), BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private JPanel crearPanelSuperior() {
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));
        panelSuperior.add(crearPanelBotones());
        panelSuperior.add(crearPanelFiltros());
        return panelSuperior;
    }


    private JPanel crearPanelBotones() {


        JPanel panelBotones = new JPanel(new GridLayout(2, 4, 10, 10)); // 3x3
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBotones.setBackground(new Color(230, 255, 240)); // verde pastel

        // Colores personalizados
        Color fondoBoton = new Color(60, 63, 65);
        Color textoBoton = new Color(230, 230, 230);
        Font fuente = new Font("Segoe UI", Font.PLAIN, 16);


        JButton btnAgregar = ControladorVista.crearBotonRedondeado(" Agregar producto", "Agregar un nuevo producto al inventario");
        JButton btnGuardar = ControladorVista.crearBotonRedondeado(" Exportar reporte en CSV", "Guardar reporte en archivo CSV");
        JButton btnVender = ControladorVista.crearBotonRedondeado(" Vender producto", "Registrar la venta de un producto");
        JButton btnVerVentas = ControladorVista.crearBotonRedondeado(" Ver Ventas", "Mostrar historial de ventas");
        JButton btnEditar = ControladorVista.crearBotonRedondeado(" Editar producto", "Editar el producto seleccionado");
        JButton btnEliminar = ControladorVista.crearBotonRedondeado(" Eliminar producto", "Eliminar el producto seleccionado");


        btnAgregar.setToolTipText("Agregar un nuevo producto al inventario");
        btnGuardar.setToolTipText("Guardar reporte en archivo CSV");
        btnVender.setToolTipText("Registrar la venta de un producto");
        btnVerVentas.setToolTipText("Mostrar historial de ventas");
        btnEditar.setToolTipText("Editar producto");
        btnEliminar.setToolTipText("Eliminar producto");

        btnAgregar.addActionListener(e ->
                controladorVista.agregarProducto(this, () -> actualizarTabla(controladorVista.obtenerProductos()))
        );

        btnVender.addActionListener(e ->
                           controladorVista.venderProducto(this, () -> actualizarTabla(controladorVista.obtenerProductos()))
        );

        btnVerVentas.addActionListener(e ->
                controladorVista.mostrarVentas(this)
        );



        btnGuardar.addActionListener(e ->
                controladorVista.guardarReportes(this)
        );




        btnEditar.addActionListener(e -> {
            controladorVista.editarProducto(this, () -> actualizarTabla(controladorVista.obtenerProductos()));
        });


        btnEliminar.addActionListener(e -> {
            controladorVista.eliminarProducto(this, () -> actualizarTabla(controladorVista.obtenerProductos()));
        });


        panelBotones.add(btnAgregar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnVender);
        panelBotones.add(btnVerVentas);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        return panelBotones;
    }


    private JPanel crearPanelFiltros() {
        JPanel panelFiltros = new JPanel();

        // Borde y con color personalizado
        Border lineaGruesa = BorderFactory.createLineBorder(new Color(40, 120, 90), 2); // verde
        TitledBorder borde = BorderFactory.createTitledBorder(
                lineaGruesa, "Filtros de búsqueda", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), new Color(40, 120, 90)); // verde

        panelFiltros.setBorder(borde);

        Font fuente = new Font("Segoe UI", Font.BOLD, 16); // Fuente

        filtroNombre = new JTextField(10);
        filtroNombre.setFont(fuente);
        //Filtro para solo admitir letras
        ((AbstractDocument) filtroNombre.getDocument()).setDocumentFilter(new FiltroTexto.SoloLetras());


        filtroPrecio = new JTextField(5);
        filtroPrecio.setFont(fuente);
        ((AbstractDocument) filtroPrecio.getDocument()).setDocumentFilter(new FiltroTexto.SoloNumeros());


        filtroCantidad = new JTextField(5);
        filtroCantidad.setFont(fuente);
        ((AbstractDocument) filtroCantidad.getDocument()).setDocumentFilter(new FiltroTexto.SoloNumeros());


        JButton btnLimpiar = ControladorVista.crearBotonRedondeado(" Limpiar filtro", "Limpiar todos los filtros");
        btnLimpiar.setToolTipText("Limpiar todos los filtros");
        btnLimpiar.addActionListener(e -> {
            limpiarFiltros();
            actualizarTabla(controladorVista.obtenerProductos());
        });

        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(fuente);
        labelNombre.setForeground(new Color(40, 120, 90));
        JLabel labelPrecio = new JLabel("Precio máximo:");
        labelPrecio.setFont(fuente);
        labelPrecio.setForeground(new Color(40, 120, 90));
        JLabel labelCantidad = new JLabel("Cantidad mínima en Stock:");
        labelCantidad.setFont(fuente);
        labelCantidad.setForeground(new Color(40, 120, 90));

        panelFiltros.setBackground(new Color(255,255,255)); // blanco

        panelFiltros.add(labelNombre);
        panelFiltros.add(filtroNombre);
        panelFiltros.add(labelPrecio);
        panelFiltros.add(filtroPrecio);
        panelFiltros.add(labelCantidad);
        panelFiltros.add(filtroCantidad);
        panelFiltros.add(btnLimpiar);


        return panelFiltros;
    }


    private void configurarFiltrosDinamicos() {
        // Timer que se reinicia cada vez que el usuario escribe
        Timer debounceTimer = new Timer(500, e -> {
            controladorVista.aplicarFiltros(
                    filtroNombre.getText(),
                    filtroPrecio.getText(),
                    filtroCantidad.getText(),
                    this::actualizarTabla,
                    mensajeError -> {
                        System.out.println("Error de filtro: " + mensajeError);
                    }
            );
        });

        debounceTimer.setRepeats(false); // una vez después del delay

        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                debounceTimer.restart(); // reinicia el delay cada vez que se escribe
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                debounceTimer.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                debounceTimer.restart();
            }
        };

        filtroNombre.getDocument().addDocumentListener(listener);
        filtroPrecio.getDocument().addDocumentListener(listener);
        filtroCantidad.getDocument().addDocumentListener(listener);
    }



    private JScrollPane crearTablaScroll() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"Nombre", "Precio", "Costo", "Cantidad disponible", "Extra"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setRowHeight(30); // letra grande

        // Mostrar cuadrícula
        tabla.setShowGrid(true);
        tabla.setGridColor(new Color(220, 220, 220));
        tabla.setIntercellSpacing(new Dimension(1, 1));

        tabla.setFillsViewportHeight(true);
        tabla.setBorder(BorderFactory.createEmptyBorder());

        tabla.setBackground(new Color(245, 255, 250));
        tabla.setForeground(new Color(20, 60, 50));

        //Encabezado con letra grande y en negrita
        Font fuenteEncabezado = tabla.getTableHeader().getFont().deriveFont(Font.BOLD, 16f);
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setFont(fuenteEncabezado);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBackground(new Color(16, 78, 42));
                label.setForeground(new Color(255, 255, 255));
                label.setOpaque(true);
                return label;
            }
        };

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Celdas con letra grande
        Font fuenteCeldas = tabla.getFont().deriveFont(16);
        tabla.setFont(fuenteCeldas);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setFont(fuenteCeldas);

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return new JScrollPane(tabla);
    }

    private void actualizarTabla(List<Producto> lista) {
        modeloTabla.setRowCount(0);
        for (Producto p : lista) {
            modeloTabla.addRow(new Object[]{
                    p.getNombre(),
                    p.getPrecioVenta(),
                    p.getCostoProduccion(),
                    p.getCantidad(),
                    p instanceof panaderia.modelo.Pan ?
                            (((panaderia.modelo.Pan) p).isTieneQueso() ? "Queso" : "No") :
                            (((panaderia.modelo.Galleta) p).isTieneChispas() ? "Chispas de Chocolate" : "No")
            });
        }
    }

    private void limpiarFiltros() {
        filtroNombre.setText("");
        filtroPrecio.setText("");
        filtroCantidad.setText("");
    }
}
