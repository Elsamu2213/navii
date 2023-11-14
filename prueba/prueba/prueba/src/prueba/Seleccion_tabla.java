package prueba;


	import javax.swing.*;
	import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

	public class Seleccion_tabla {
		 public static void crearVentana(String string) {
	        SwingUtilities.invokeLater(() -> createAndShowGUI());
	    }

	    private static void createAndShowGUI() {
	        JFrame frame = new JFrame("Ejemplo de JComboBox");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        // Crear el panel izquierdo (bases de datos)
	        JPanel panelIzquierdo = new JPanel();
	        panelIzquierdo.setLayout(new BorderLayout());
	        panelIzquierdo.setBackground(Color.pink);

	        JMenuBar menuBar = new JMenuBar();
	        JMenu menu = new JMenu("Menú Secundario");

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

	        // Crear un JPanel para el nombre del JComboBox
	        JPanel comboBoxPanel = new JPanel();
	        JLabel comboBoxLabel = new JLabel("Selecciona una opción:");
	        comboBoxPanel.add(comboBoxLabel);

	        // Crear un JComboBox y agregar elementos
	        JComboBox<String> comboBox = new JComboBox<>();
	        comboBox.addItem("Opción 1");
	        comboBox.addItem("Opción 2");
	        comboBox.addItem("Opción 3");

	        // Agregar un ActionListener para manejar eventos de selección
	        comboBox.addActionListener(e -> {
	            String selectedOption = (String) comboBox.getSelectedItem();
	            JOptionPane.showMessageDialog(frame, "Has seleccionado: " + selectedOption);
	        });

	        // Crear un JSplitPane para dividir la ventana
	        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, comboBoxPanel, comboBox);
	        frame.add(splitPane);

	        frame.setSize(800, 400);
	        frame.setVisible(true);
	    }
	}


