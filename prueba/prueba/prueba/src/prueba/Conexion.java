package prueba;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Conexion {
    private Connection conexionMysQl;
    
    public void conexión(String host, String puerto, String usuario, String contraseña) throws SQLException { 
        String url = "jdbc:mysql://" + host + ":" + puerto+"/"; 
        conexionMysQl = DriverManager.getConnection(url, usuario, contraseña);
        JOptionPane.showMessageDialog(null, "Se estableció la conexión a MySQL");
    }
}
