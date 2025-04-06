package panaderia.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class VentanaPrincipal extends JFrame {

    private ArrayList<String> productos = new ArrayList<String>();
    private ArrayList<String> ventas = new ArrayList<String>();

    private JTextArea textArea;

    public VentanaPrincipal() {
        setTitle("Tienda Virtual");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel para botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(2, 2, 10, 10));

        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnVender = new JButton("Vender Producto");
        JButton btnMostrarProductos = new JButton("Mostrar Productos");
        JButton btnMostrarVentas = new JButton("Mostrar Ventas");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnVender);
        panelBotones.add(btnMostrarProductos);
        panelBotones.add(btnMostrarVentas);

        // Área de texto para mostrar resultados
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);

        add(panelBotones, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // Acción: Agregar producto
        btnAgregar.addActionListener(e -> {
            String producto = JOptionPane.showInputDialog("Ingrese nombre del producto:");
            if (producto != null && !producto.trim().isEmpty()) {
                productos.add(producto);
                textArea.setText("Producto agregado: " + producto);
            }
        });

        // Acción: Vender producto
        btnVender.addActionListener(e -> {
            if (productos.isEmpty()) {
                textArea.setText("No hay productos disponibles para vender.");
                return;
            }
            String producto = (String) JOptionPane.showInputDialog(null,
                    "Seleccione producto para vender:",
                    "Venta",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    productos.toArray(),
                    productos.get(0));
            if (producto != null) {
                productos.remove(producto);
                ventas.add(producto);
                textArea.setText("Producto vendido: " + producto);
            }
        });

        // Acción: Mostrar productos
        btnMostrarProductos.addActionListener(e -> {
            if (productos.isEmpty()) {
                textArea.setText("No hay productos disponibles.");
            } else {
                textArea.setText("Productos disponibles:\n" + String.join("\n", productos));
            }
        });

        // Acción: Mostrar ventas
        btnMostrarVentas.addActionListener(e -> {
            if (ventas.isEmpty()) {
                textArea.setText("No hay ventas registradas.");
            } else {
                textArea.setText("Ventas realizadas:\n" + String.join("\n", ventas));
            }
        });
    }}