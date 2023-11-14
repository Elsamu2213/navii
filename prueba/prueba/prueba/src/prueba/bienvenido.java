package prueba;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class bienvenido {
    private static JFrame ventanaActual;
    private static JMenu menu;
    private static JMenuItem menuItem1;
    private static JMenuItem menuItem2;
    private static JMenuItem menuItem3;
    private static JMenuItem menuItem4;
    private static JMenuItem menuItem5;
    private static JPanel panelDerecho;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> crearVentanaInicio());
    }

    public static void crearVentanaInicio() {
    	
        JFrame frame = new JFrame("EPILEPSIA SQL ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        

        // Crear un JSplitPane para dividir la ventana en dos paneles
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        
     // Panel derecho (contenido)
        

        JPanel panelDerecho = new JPanel();
        panelDerecho = new JPanel();
        panelDerecho.setLayout(new BorderLayout());
        panelDerecho.setBackground(Color.white);
        
     // Componente predeterminado (puedes personalizarlo según tus necesidades)
        JLabel defaultLabel = new JLabel("Contenido predeterminado");
        defaultLabel.setHorizontalAlignment(JLabel.CENTER);
        panelDerecho.add(defaultLabel, BorderLayout.CENTER);
        
        
        // Panel izquierdo (con imagen)
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BorderLayout());
        panelIzquierdo.setBackground(new Color(169, 169, 169)); // Gris claro

     // Cambia el color del panel izquierdo al pasar el ratón
        panelIzquierdo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panelIzquierdo.setBackground(Color.red);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panelIzquierdo.setBackground(new Color(169, 169, 169)); // Gris claro al salir del ratón
            }
        });

     // Agrega un MouseMotionListener para cambiar el color también mientras se mueve el ratón
        panelIzquierdo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                // Cambia el color del panel izquierdo según la posición del ratón
                int mouseX = evt.getX();
                int panelWidth = panelIzquierdo.getWidth();
                Color newColor;

                if (mouseX < panelWidth / 3) {
                    newColor = Color.blue;
                } else if (mouseX < (2 * panelWidth) / 3) {
                    newColor = Color.red;
                } else {
                    newColor = Color.yellow;
                }

                panelIzquierdo.setBackground(newColor);
            }
        });
        
        JMenuBar menuBar = new JMenuBar();
        menu = new JMenu("Menú");
        menuBar.add(menu);

        menuItem1 = crearMenuItem("Conexión");
        menuItem2 = crearMenuItem("Crear BDD's");
        menuItem3 = crearMenuItem("Seleccionar tabla");
        menuItem4 = crearMenuItem("Crear tabla");
        menuItem5 = crearMenuItem("Tabla");

        menuItem1.setEnabled(true);
        menuItem2.setEnabled(false);
        menuItem3.setEnabled(false);
        menuItem4.setEnabled(false);
        menuItem5.setEnabled(false);

        menu.add(menuItem1);
        menu.add(menuItem2);
        menu.add(menuItem3);
        menu.add(menuItem4);
        menu.add(menuItem5);
        menuBar.add(menu);

        panelIzquierdo.add(menuBar, BorderLayout.NORTH);
        

        // Panel derecho (contenido)-------------------------------------------------------------------------------------
      
        panelDerecho = new JPanel();
        panelDerecho.setLayout(new BorderLayout());
        panelDerecho.setBackground(Color.white);

        ImageIcon gifIcon = new ImageIcon(bienvenido.class.getResource("/prueba/33img.gif"));
        JLabel gifLabel = new JLabel(gifIcon);
        panelDerecho.add(gifLabel, BorderLayout.CENTER);

        // Agregar el JSplitPane al contenido del JFrame
        splitPane.setLeftComponent(panelIzquierdo);
        splitPane.setRightComponent(panelDerecho);
        frame.getContentPane().add(splitPane);
        splitPane.setDividerLocation(300);

        frame.setVisible(true);
    }

    private static JMenuItem crearMenuItem(String text) {
    	
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setFont(new Font("Arial", Font.PLAIN, 16));
        menuItem.setForeground(new Color(0, 0, 0)); // Color de texto
        menuItem.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.CYAN));
     // Agrega un JLabel con un GIF al panel derecho
        
        
        


        menuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuItem.setBackground(new Color(192, 192, 192)); // Gris más claro al pasar el ratón
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuItem.setBackground(null); // Restaurar el color original al salir del ratón
            }
        });
        
        //menu de-----------------------------------------------------------------------------------------

        menuItem.addActionListener(new ActionListener() {
        	
        	
        	
            public void actionPerformed(ActionEvent e) {
            	
            	 menuItem1.addActionListener(new ActionListener() {
                     public void actionPerformed(ActionEvent e) {
                         cerrarVentanaActual();
                         conectar_BD.crearVentana("Ventana 2");
                         menuItem1.setBackground(Color.PINK);
                     }
                 });
                
                
                
                
                

                menuItem2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cerrarVentanaActual();
                        Creacion_BD.crearVentana("Ventana 2");
                    }
                });

                menuItem3.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cerrarVentanaActual();
                        Seleccion_tabla.crearVentana("ventana 3");
                    }
                });
                menuItem4.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cerrarVentanaActual();
                        Nombre_tabla.crearVentana("Ventana  4");
                    }
                });
                menuItem5.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        cerrarVentanaActual();
                        tablas.crearVentana("Ventana 5");
                    }
                });
            }
        });

        return menuItem;
    }

    private static void cerrarVentanaActual() {
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }
    }
}
