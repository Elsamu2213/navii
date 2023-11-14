package prueba;
import javax.swing.*;

import prueba.Mostrar_Tablas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Creacion_BD {
    private static DefaultListModel<String> dbListModel = new DefaultListModel<>();
    private static JList<String> dbList = new JList<>(dbListModel);

    public static void crearVentana(String string) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Creación de la Base de Datos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);

        // Crear un JPanel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Crear un JPanel para el cuadro de texto y su etiqueta
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setPreferredSize(new Dimension(300, 100));

        JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel textLabel = new JLabel("Nombre de la base de datos:");

        JTextField textField = new JTextField(15);

        textPanel.add(textLabel);
        textPanel.add(textField);

        // Botón para crear la base de datos
        JButton createButton = new JButton("Nueva Base de Datos");
        createButton.setPreferredSize(new Dimension(190, 20));
        createButton.setBackground(Color.PINK);
        createButton.addActionListener(e -> {
            String dbName = textField.getText();
            if (!dbName.isEmpty()) {
                try {
                    // Establecer una conexión a la base de datos existente (ejemplo de MySQL)
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "123456789");

                    // Crear la consulta SQL para crear la base de datos
                    String createDBQuery = "CREATE DATABASE " + dbName;

                    // Ejecutar la consulta SQL
                    Statement statement = conn.createStatement();
                    statement.executeUpdate(createDBQuery);

                    // Cerrar la conexión y limpiar el cuadro de texto
                    conn.close();
                    textField.setText("");

                    // Mostrar un mensaje de éxito
                    JOptionPane.showMessageDialog(frame, "Base de datos creada con éxito: " + dbName, "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Actualizar la lista de bases de datos en el panel izquierdo
                    actualizarListaBasesDatos();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // Manejo de errores en caso de falla de la conexión o consulta SQL
                    JOptionPane.showMessageDialog(frame, "Error al crear la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        centerPanel.add(textPanel);
        centerPanel.add(createButton);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Botón para Eliminar la base de datos seleccionada
        JButton deleteButton = new JButton("Eliminar Base de Datos");
        deleteButton.setPreferredSize(new Dimension(150, 30));
        deleteButton.setBackground(Color.PINK);
        deleteButton.addActionListener(e -> {
            int selectedIndex = dbList.getSelectedIndex();
            if (selectedIndex >= 0) {
                String selectedDB = dbListModel.getElementAt(selectedIndex);
                eliminarBaseDeDatos(selectedDB);
            } else {
                JOptionPane.showMessageDialog(frame, "Selecciona una base de datos de la lista primero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(deleteButton, BorderLayout.SOUTH);

        // Agregar los paneles al panel principal
        mainPanel.add(textPanel, BorderLayout.NORTH);

        // Crear un JPanel para mostrar las bases de datos en el lado izquierdo
        JPanel dbPanel = new JPanel(new BorderLayout());
        dbPanel.add(new JLabel("Bases de Datos Creadas:"), BorderLayout.NORTH);
        dbPanel.add(new JScrollPane(dbList), BorderLayout.CENTER);
        dbPanel.setBackground(Color.BLUE); // Establecer el color de fondo a azul

        // Agregar los paneles al JFrame
        frame.add(mainPanel, BorderLayout.CENTER); // Panel principal al centro
        frame.add(dbPanel, BorderLayout.WEST); // Panel de bases de datos a la izquierda
        frame.setSize(800, 400);
        frame.setVisible(true);

        // Actualizar la lista de bases de datos al iniciar la aplicación
        actualizarListaBasesDatos();

        // Agregar manejador de eventos para la lista de bases de datos
        dbList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int selectedIndex = dbList.getSelectedIndex();
                    if (selectedIndex >= 0) {
                        String selectedDB = dbListModel.getElementAt(selectedIndex);
                        mostrarCreacionDeTablas(selectedDB);
                    }
                }
            }
        });

    }

    private static void mostrarCreacionDeTablas(String dbName) {
        Mostrar_Tablas tablas = new Mostrar_Tablas(dbName);
        tablas.setVisible(true);
    }

    // Método para actualizar la lista de bases de datos
    private static void actualizarListaBasesDatos() {
        dbListModel.clear(); // Borrar elementos existentes
        try {
            // Establecer una conexión a la base de datos existente (ejemplo de MySQL)
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "123456789");

            // Obtener metadatos de la base de datos
            DatabaseMetaData metaData = conn.getMetaData();

            // Obtener el resultado de la consulta de todas las bases de datos
            ResultSet resultSet = metaData.getCatalogs();

            // Agregar los nombres de las bases de datos a la lista
            while (resultSet.next()) {
                String dbName = resultSet.getString("TABLE_CAT");
                dbListModel.addElement(dbName);
            }

            // Cerrar la conexión
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Manejo de errores en caso de falla de la conexión o consulta SQL
        }
    }

    // Método para eliminar la base de datos seleccionada
    private static void eliminarBaseDeDatos(String dbName) {
        try {
            // Establecer una conexión a la base de datos existente (ejemplo de MySQL)
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "123456789");

            // Crear la consulta SQL para eliminar la base de datos
            String dropDBQuery = "DROP DATABASE " + dbName;

            // Ejecutar la consulta SQL
            Statement statement = conn.createStatement();
            statement.executeUpdate(dropDBQuery);

            // Cerrar la conexión
            conn.close();

            // Mostrar un mensaje de éxito
            JOptionPane.showMessageDialog(null, "Base de datos eliminada con éxito: " + dbName, "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Actualizar la lista de bases de datos en el panel izquierdo
            actualizarListaBasesDatos();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Manejo de errores en caso de falla de la conexión o consulta SQL
            JOptionPane.showMessageDialog(null, "Error al eliminar la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        crearVentana("Database Manager");
    }
}
