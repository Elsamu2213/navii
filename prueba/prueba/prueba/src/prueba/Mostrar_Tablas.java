package prueba;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class Mostrar_Tablas extends JFrame {
    private String baseDeDatos;
    private JTextField nombreTablaTextField;
    private JSpinner columnasSpinner;
    private JButton crearTablaButton;
    private JButton agregarDatoButton;
    private DefaultTableModel tableModel;
    private JTable dataTable;
    //eliminar
    private JButton eliminarButton; 
    //consultar
    private JButton consultaButton;
    //
    //Mostrar
    private JButton mostrarButton;

    public Mostrar_Tablas(String dbName) {
        this.baseDeDatos = dbName;
        setTitle("Administrar Tablas");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel createTablePanel = new JPanel();
        createTablePanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nombreTablaLabel = new JLabel("Nombre de la Tabla:");
        nombreTablaTextField = new JTextField(20);

        JLabel columnasLabel = new JLabel("Columnas a agregar:");
        columnasSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 15, 1));

        crearTablaButton = new JButton("Crear Tabla");
        agregarDatoButton = new JButton("Insertar");
       //eliminar 
        eliminarButton = new JButton("Eliminar");
        // consultar
        consultaButton = new JButton("Consulta");
        //mostrar
        mostrarButton = new JButton("Mostrar");

        createTablePanel.add(nombreTablaLabel);
        createTablePanel.add(nombreTablaTextField);
        createTablePanel.add(columnasLabel);
        createTablePanel.add(columnasSpinner);
        createTablePanel.add(new JLabel());
        createTablePanel.add(crearTablaButton);
        createTablePanel.add(new JLabel());
        createTablePanel.add(agregarDatoButton);
        createTablePanel.add(new JLabel());
        createTablePanel.add(eliminarButton); //eliminar
        createTablePanel.add(new JLabel());
        createTablePanel.add(consultaButton); // consultar
        createTablePanel.add(new JLabel());
        createTablePanel.add(mostrarButton);  //mostar
        
       

        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);

        mainPanel.add(createTablePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    
        add(mainPanel);

        crearTablaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                crearTabla();
            }
        });

        agregarDatoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertarDatosButtonActionPerformed(e);
            }
        });
        //eliminar
        eliminarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarDatoSeleccionado();
            }
        });
        //consulta
        consultaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirVentanaConsulta(); // Abrir la ventana de consulta
            }
        });
        mostrarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarDatos();
            }
        }); //mostrar
      
    }

    private void abrirVentanaConsulta() {
        JFrame ventanaConsulta = new JFrame("Ventana de Consulta");
        ventanaConsulta.setSize(600, 400);
        ventanaConsulta.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaConsulta.setLocationRelativeTo(null);

        JPanel panelConsulta = new JPanel();
        panelConsulta.setLayout(new BorderLayout());
        JTextArea areaTextoConsulta = new JTextArea();
        areaTextoConsulta.setWrapStyleWord(true);
        areaTextoConsulta.setLineWrap(true);
        JScrollPane scrollConsulta = new JScrollPane(areaTextoConsulta);
        panelConsulta.add(scrollConsulta, BorderLayout.CENTER);

        // Agregar listas de tablas y columnas
        JList<String> tablasList = new JList<>();
        JList<String> columnasList = new JList<>();
        JScrollPane tablasScrollPane = new JScrollPane(tablasList);
        JScrollPane columnasScrollPane = new JScrollPane(columnasList);

        panelConsulta.add(tablasScrollPane, BorderLayout.WEST);
        panelConsulta.add(columnasScrollPane, BorderLayout.EAST);

        // Obtener la lista de tablas disponibles en la base de datos
        List<String> tablas = obtenerTablasDisponibles();
        DefaultListModel<String> tablasListModel = new DefaultListModel<>();
        for (String tabla : tablas) {
            tablasListModel.addElement(tabla);
        }
        tablasList.setModel(tablasListModel);

        tablasList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Cuando se seleccione una tabla, cargar las columnas de esa tabla
                    String tablaSeleccionada = tablasList.getSelectedValue();
                    List<String> columnas = obtenerColumnasDeTabla(tablaSeleccionada);
                    DefaultListModel<String> columnasListModel = new DefaultListModel<>();
                    for (String columna : columnas) {
                        columnasListModel.addElement(columna);
                    }
                    columnasList.setModel(columnasListModel);
                }
            }
        });
        
        JButton ejecutarConsultaButton = new JButton("Ejecutar Consulta");
        ejecutarConsultaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String tablaSeleccionada = tablasList.getSelectedValue();
                DefaultListModel<String> columnasListModel = (DefaultListModel<String>) columnasList.getModel();
                List<String> columnasSeleccionadas = columnasList.getSelectedValuesList();

                if (tablaSeleccionada != null && !columnasSeleccionadas.isEmpty()) {
                    StringBuilder consultaSQL = new StringBuilder("SELECT ");
                    for (String columna : columnasSeleccionadas) {
                        consultaSQL.append(columna).append(", ");
                    }
                    consultaSQL.setLength(consultaSQL.length() - 2); // Elimina la última coma y espacio
                    consultaSQL.append(" FROM ").append(tablaSeleccionada);

                    areaTextoConsulta.setText(consultaSQL.toString());
                    
                    // Ahora, mostraremos los datos en un JTable
                    mostrarDatosConsulta(consultaSQL.toString());
                } else {
                    JOptionPane.showMessageDialog(ventanaConsulta, "Selecciona una tabla y al menos una columna.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        panelConsulta.add(ejecutarConsultaButton, BorderLayout.SOUTH);
        ventanaConsulta.add(panelConsulta);

        ventanaConsulta.setVisible(true);
    }
    private void mostrarDatosConsulta(String consultaSQL) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDeDatos, "root", "123456789");
            ResultSet resultSet = conn.createStatement().executeQuery(consultaSQL);
            
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            while (resultSet.next()) {
                Vector<Object> rowData = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.add(resultSet.getObject(i));
                }
                tableModel.addRow(rowData);
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar datos en la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<String> obtenerTablasDisponibles() {
        List<String> tablas = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDeDatos, "root", "123456789");
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});

            while (tables.next()) {
                tablas.add(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return tablas;
    }

    private List<String> obtenerColumnasDeTabla(String nombreTabla) {
        List<String> columnas = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDeDatos, "root", "123456789");
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, nombreTabla, null);

            while (columns.next()) {
                columnas.add(columns.getString("COLUMN_NAME"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return columnas;
    }

    private void mostrarDatosEnTabla(String nombreTabla) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDeDatos, "root", "123456789");
            Statement statement = conn.createStatement();

            String consulta = "SELECT * FROM " + nombreTabla;
            ResultSet resultSet = statement.executeQuery(consulta);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar datos en la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearTabla() {
        String nombreTabla = nombreTablaTextField.getText();
        int cantidadColumnas = (int) columnasSpinner.getValue();

        if (nombreTabla.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar un nombre para la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE ");
        createTableSQL.append(nombreTabla).append(" (");

        for (int i = 0; i < cantidadColumnas; i++) {
            if (i > 0) {
                createTableSQL.append(", ");
            }

            String nombreColumna = JOptionPane.showInputDialog(this, "Nombre Columna " + (i + 1) + ":");
            if (nombreColumna == null || nombreColumna.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debes ingresar un nombre para la columna.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String tipoDato = (String) JOptionPane.showInputDialog(this, "Tipo de Dato Columna " + (i + 1) + ":", "Tipo de Dato",
                    JOptionPane.QUESTION_MESSAGE, null, new String[]{"INT", "VARCHAR(255)", "FLOAT", "DATE", "BLOB", "BOOLEAN"}, "INT");

            createTableSQL.append(nombreColumna).append(" ").append(tipoDato);
        }

        createTableSQL.append(");");

        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDeDatos, "root", "123456789");

            Statement statement = conn.createStatement();
            statement.executeUpdate(createTableSQL.toString());

            String mensaje = "La tabla '" + nombreTabla + "' ha sido creada en la base de datos '" + baseDeDatos + "'.";
            JOptionPane.showMessageDialog(this, mensaje, "Tabla Creada", JOptionPane.INFORMATION_MESSAGE);

            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            mostrarDatosEnTabla(nombreTabla);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al crear la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void insertarDatosButtonActionPerformed(ActionEvent evt) {
        String nombreTabla = nombreTablaTextField.getText();

        if (nombreTabla.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar un nombre de tabla válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidadColumnas = obtenerCantidadColumnas(nombreTabla);

        if (cantidadColumnas == -1) {
            JOptionPane.showMessageDialog(this, "Error al obtener la cantidad de columnas de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Object> datos = new ArrayList<>();

        for (int i = 0; i < cantidadColumnas; i++) {
            String dato = JOptionPane.showInputDialog(this, "Ingrese el dato para la columna " + (i + 1) + ":");
            if (dato == null) {
                // El usuario presionó cancelar
                return;
            }
            datos.add(dato);
        }

        if (datos.size() != cantidadColumnas) {
            JOptionPane.showMessageDialog(this, "Debes ingresar un dato para cada columna.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDeDatos, "root", "123456789");

            StringBuilder insertDataSQL = new StringBuilder("INSERT INTO " + nombreTabla + " VALUES (");

            for (int i = 0; i < cantidadColumnas; i++) {
                if (i > 0) {
                    insertDataSQL.append(", ");
                }

                insertDataSQL.append("?");
            }

            insertDataSQL.append(");");

            preparedStatement = conn.prepareStatement(insertDataSQL.toString());

            for (int i = 0; i < cantidadColumnas; i++) {
                preparedStatement.setObject(i + 1, datos.get(i));
            }

            preparedStatement.executeUpdate();

            mostrarDatosEnTabla(nombreTabla);

            JOptionPane.showMessageDialog(this, "Datos agregados con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al agregar datos a la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    private void eliminarDatoSeleccionado() {
    	int[] filasSeleccionadas = dataTable.getSelectedRows();
        if (filasSeleccionadas.length > 0) {
            String nombreTabla = nombreTablaTextField.getText();
            int columnaID = 0; // Reemplaza 0 con el índice de la columna que contiene el ID o la clave primaria

            int registrosEliminados = 0;
            Connection conn = null;
            PreparedStatement preparedStatement = null;

            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDeDatos, "root", "123456789");

                for (int fila : filasSeleccionadas) {
                    Object valorColumnaID = dataTable.getValueAt(fila, columnaID);

                    if (valorColumnaID != null) {
                        String sql = "DELETE FROM " + nombreTabla + " WHERE ID = ?"; // Reemplaza "ID" con el nombre de la columna de clave primaria

                        preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setObject(1, valorColumnaID);

                        int resultado = preparedStatement.executeUpdate();
                        if (resultado > 0) {
                            registrosEliminados++;
                        }
                    }
                }

                mostrarDatosEnTabla(nombreTabla);

                if (registrosEliminados > 0) {
                    JOptionPane.showMessageDialog(this, registrosEliminados + " registro(s) eliminado(s) con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudieron eliminar los registros seleccionados.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar registros: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona al menos una fila para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void mostrarDatos() {
    	 String nombreTabla = nombreTablaTextField.getText();
         if (nombreTabla.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Debes ingresar un nombre de tabla válido.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
         }

         String consulta = "SELECT * FROM " + nombreTabla;

         try {
             Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDeDatos, "root", "123456789");
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(consulta);

             ResultSetMetaData metaData = resultSet.getMetaData();
             int columnCount = metaData.getColumnCount();

             tableModel.setRowCount(0);
             tableModel.setColumnCount(0);

             for (int i = 1; i <= columnCount; i++) {
                 tableModel.addColumn(metaData.getColumnName(i));
             }

             while (resultSet.next()) {
                 Object[] rowData = new Object[columnCount];
                 for (int i = 1; i <= columnCount; i++) {
                     rowData[i - 1] = resultSet.getObject(i);
                 }
                 tableModel.addRow(rowData);
             }

             conn.close();
         } catch (SQLException ex) {
             ex.printStackTrace();
             JOptionPane.showMessageDialog(this, "Error al cargar datos en la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
         }

      }
    
    private void ejecutarConsulta(String consulta) {
        // Agregar lógica para ejecutar la consulta en la base de datos
        // y mostrar los resultados en la ventana de consulta.
        // Asegúrate de manejar las conexiones y errores adecuadamente.
        // Puedes usar JDBC para ejecutar la consulta en la base de datos.
        // Aquí solo se muestra cómo abrir la ventana de consulta.
    }
    
    private int obtenerCantidadColumnas(String nombreTabla) {
        int cantidadColumnas = -1;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + baseDeDatos, "root", "123456789");
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, nombreTabla, null);
            cantidadColumnas = 0;
            while (resultSet.next()) {
                cantidadColumnas++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return cantidadColumnas;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Mostrar_Tablas("NombreDeTuBaseDeDatos");
            }
        });
    }
}
