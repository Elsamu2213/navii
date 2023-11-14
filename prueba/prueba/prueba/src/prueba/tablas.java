package prueba;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

	public class tablas {
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

	        // Crea el panel derecho
	        JPanel mainPanel = new JPanel();
	        mainPanel.setLayout(new GridBagLayout());

	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes


	        

	        frame.add(mainPanel);
	        frame.setVisible(true);
	        frame.add(panelIzquierdo, BorderLayout.WEST);
	    }
	}
