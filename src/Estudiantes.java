import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Estudiantes extends JFrame {

    private JPanel Panel;
    private JTextField IdtxtField;
    private JTextField NombretxtField;
    private JTextField ApellidotxtField;
    private JTextField EdadtxtField;
    private JTextField TelefonotxtField;
    private JButton ingresarBtn;
    private JButton consultarBtn;
    private JList Lista;
    private JTextField CarreratxtField;
    Connection con;
    PreparedStatement ps;
    DefaultListModel mod = new DefaultListModel();
    Statement st;
    ResultSet r;

    public Estudiantes() {
        consultarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    listar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ingresarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    insertar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void listar() throws SQLException {
        conectar();
        Lista.setModel(mod);
        st = con.createStatement();
        r = st.executeQuery("SELECT id,nombre,apellido,edad,telefono,carrera FROM estudiante");
        mod.removeAllElements();
        while (r.next()) {
            mod.addElement(r.getString(1) + " " + r.getString(2) + " " + r.getString(3));
        }
    }

    public void insertar() throws SQLException {
        conectar();
        ps = con.prepareStatement("INSERT INTO estudiante VALUES (?,?,?,?,?,?)");
        ps.setInt(1, Integer.parseInt(IdtxtField.getText()));
        ps.setString(2, NombretxtField.getText());
        ps.setString(3, ApellidotxtField.getText());
        ps.setInt(4, Integer.parseInt(EdadtxtField.getText()));
        ps.setString(5, TelefonotxtField.getText());
        ps.setString(6, CarreratxtField.getText());
        if (ps.executeUpdate() > 0) {
            Lista.setModel(mod);
            mod.removeAllElements();
            mod.addElement("! Insercion Exitosa");
            IdtxtField.setText(" ");
            NombretxtField.setText(" ");
            ApellidotxtField.setText(" ");
            EdadtxtField.setText(" ");
            TelefonotxtField.setText(" ");
            CarreratxtField.setText(" ");
        }
    }

    public static void main(String[] args) {
        Estudiantes f = new Estudiantes();
        f.setContentPane(new Estudiantes().Panel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.pack();
    }

    public void conectar() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/learning", "root", "250402");
            System.out.println("Conectado");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
