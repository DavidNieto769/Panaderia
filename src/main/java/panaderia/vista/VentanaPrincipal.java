package panaderia.vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;

import panaderia.controlador.ControladorInventario;
import panaderia.controlador.ControladorVista;
import panaderia.modelo.Producto;
import panaderia.utilidades.FiltroSoloLetras;

import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private final ControladorVista controladorVista;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextField filtroNombre, filtroPrecio, filtroCantidad;

    public VentanaPrincipal() {
        ControladorInventario controlador = new ControladorInventario();
        controladorVista = new ControladorVista(controlador);

        setTitle("Gestión de Panadería");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage("icono_panaderia.png"));

        initComponents();
        actualizarTabla(controladorVista.obtenerProductos());
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
        JPanel panelBotones = new JPanel();

        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnLimpiar = new JButton("Limpiar Filtros");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnVender = new JButton("Vender Producto");
        JButton btnVerVentas = new JButton("Ver Ventas");

        btnAgregar.setToolTipText("Agregar un nuevo producto al inventario");
        btnFiltrar.setToolTipText("Aplicar filtros según nombre, precio o cantidad");
        btnLimpiar.setToolTipText("Limpiar todos los filtros");
        btnGuardar.setToolTipText("Guardar reporte en archivo CSV");
        btnVender.setToolTipText("Registrar la venta de un producto");
        btnVerVentas.setToolTipText("Mostrar historial de ventas");

        btnAgregar.addActionListener(e ->
                controladorVista.agregarProducto(this, () -> actualizarTabla(controladorVista.obtenerProductos()))
        );

        btnVender.addActionListener(e ->
                controladorVista.venderProducto(this, () -> actualizarTabla(controladorVista.obtenerProductos()))
        );

        btnVerVentas.addActionListener(e ->
                controladorVista.mostrarVentas(this)
        );

        btnFiltrar.addActionListener(e ->
                controladorVista.aplicarFiltros(
                        filtroNombre.getText(),
                        filtroPrecio.getText(),
                        filtroCantidad.getText(),
                        this::actualizarTabla,
                        mensajeError -> JOptionPane.showMessageDialog(this, mensajeError, "Error", JOptionPane.ERROR_MESSAGE)
                )
        );

        btnLimpiar.addActionListener(e -> {
            limpiarFiltros();
            actualizarTabla(controladorVista.obtenerProductos());
        });


        btnGuardar.addActionListener(e ->
                controladorVista.guardarReportes(this)
        );

        panelBotones.add(btnAgregar);
        panelBotones.add(btnFiltrar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnVender);
        panelBotones.add(btnVerVentas);

        return panelBotones;
    }

    private JPanel crearPanelFiltros() {
        JPanel panelFiltros = new JPanel();
        panelFiltros.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Filtros de búsqueda", TitledBorder.LEFT, TitledBorder.TOP));

        filtroNombre = new JTextField(10);
        ((AbstractDocument) filtroNombre.getDocument()).setDocumentFilter(new FiltroSoloLetras());

        filtroPrecio = new JTextField(5);
        filtroCantidad = new JTextField(5);

        panelFiltros.add(new JLabel("Nombre:"));
        panelFiltros.add(filtroNombre);
        panelFiltros.add(new JLabel("Precio máximo:"));
        panelFiltros.add(filtroPrecio);
        panelFiltros.add(new JLabel("Cantidad mínima:"));
        panelFiltros.add(filtroCantidad);

        return panelFiltros;
    }

    private JScrollPane crearTablaScroll() {
        modeloTabla = new DefaultTableModel(new Object[]{"Nombre", "Precio", "Costo", "Cantidad", "Extra"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFillsViewportHeight(true);

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
                            (((panaderia.modelo.Pan) p).isTieneQueso() ? "Sí" : "No") :
                            (((panaderia.modelo.Galleta) p).isTieneChispas() ? "Sí" : "No")
            });
        }
    }

    private void limpiarFiltros() {
        filtroNombre.setText("");
        filtroPrecio.setText("");
        filtroCantidad.setText("");
    }

}
