package prueba;

import java.awt.BorderLayout;
import javax.swing.JOptionPane;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import prueba.Conexion;
import prueba.Creacion_BD;



public class conectar_BD {
	private static JFrame frame;
    private static JTextField userTextField;
    private static JTextField passwordTextField;
    private static JTextField hostTextField;
    private static JTextField puertoTextField;
    private static JButton connectButton;

    public static void main(String[] args) {
        crearVentana("Database Manager");
    }

    public static void crearVentana(String titulo) {
        frame = new JFrame(titulo);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);

        // Crear el panel izquierdo (bases de datos)
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BorderLayout());
        panelIzquierdo.setBackground(Color.blue);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Conexion");

        JMenuItem menuItemVolver = new JMenuItem("Volver a la Ventana de Inicio");
        JMenuItem menuItemConectarBD = new JMenuItem("Conectar a la Base de Datos");

        menuItemVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // main.crearVentanaInicio();
            }
        });

        menu.add(menuItemVolver);
        menuBar.add(menu);

        panelIzquierdo.add(menuBar, BorderLayout.NORTH);

        // Crea el panel derecho
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes

        JLabel userLabel = new JLabel("Usuario:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(userLabel, gbc);

        userTextField = new JTextField(10);
        gbc.gridx = 1;
        mainPanel.add(userTextField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(passwordLabel, gbc);

        passwordTextField = new JPasswordField(10); // Usar JPasswordField para contraseñas
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(passwordTextField, gbc);

        JLabel hostLabel = new JLabel("Host:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(hostLabel, gbc);

        hostTextField = new JTextField(10);
        gbc.gridx = 1;
        mainPanel.add(hostTextField, gbc);

        JLabel puertoLabel = new JLabel("Puerto:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(puertoLabel, gbc);

        puertoTextField = new JTextField(10);
        gbc.gridx = 1;
        mainPanel.add(puertoTextField, gbc);

        connectButton = new JButton("Conectar");
        connectButton.setPreferredSize(new Dimension(100, 30));
        connectButton.setBackground(Color.PINK);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(connectButton, gbc);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = userTextField.getText();
                String contraseña = passwordTextField.getText();
                String host = hostTextField.getText();
                String puerto = puertoTextField.getText();

                Conexion conexion = new Conexion();
                try {
                    conexion.conexión(host, puerto, usuario, contraseña);
                    
                    // Cerrar la ventana actual
                    frame.dispose();
                    
                    // Mostrar la nueva interfaz
                    Creacion_BD.crearVentana(titulo);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    // Manejo de errores en caso de falla de la conexión
                    JOptionPane.showMessageDialog(frame, "Error de conexión: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

                }
            }
        });

        

        frame.add(mainPanel);
        frame.setVisible(true);
        frame.add(panelIzquierdo, BorderLayout.WEST);
    }
}


