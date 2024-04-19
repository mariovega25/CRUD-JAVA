import javax.swing.*;
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
    private JButton ModificarBtn;
    private JButton BorrarBtn;
    private JButton CerrarConexionBtn;
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
        ModificarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedIndex = Lista.getSelectedIndex();
                if (selectedIndex != -1) { // Se ha seleccionado un elemento en la lista
                    try {
                        // Obtener los datos del estudiante seleccionado
                        String[] datos = Lista.getModel().getElementAt(selectedIndex).toString().split(" ");
                        int id = Integer.parseInt(datos[0]);
                        String nombre = datos[1];
                        String apellido = datos[2];
                        int edad = Integer.parseInt(datos[3]);
                        String telefono = datos[4];
                        String carrera = datos[5];
                        modificar();
                        // Actualizar los campos de texto con los datos del estudiante seleccionado
                        IdtxtField.setText(Integer.toString(id));
                        NombretxtField.setText(nombre);
                        ApellidotxtField.setText(apellido);
                        EdadtxtField.setText(Integer.toString(edad));
                        TelefonotxtField.setText(telefono);
                        CarreratxtField.setText(carrera);

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(Estudiantes.this, "Error al obtener los datos del estudiante", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(Estudiantes.this, "Por favor, seleccione un estudiante de la lista", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        BorrarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        CerrarConexionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarConexion();
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
            mod.addElement(r.getString(1) + " " + r.getString(2) + " " + r.getString(3)+ " " +r.getString(4)+ " " +r.getString(5)+ " " +r.getString(6));
        }

    }

    public void insertar() throws SQLException {
        conectar();
        try {
            int id = Integer.parseInt(IdtxtField.getText());
            int edad = Integer.parseInt(EdadtxtField.getText());

            ps = con.prepareStatement("INSERT INTO estudiante VALUES (?,?,?,?,?,?)");
            ps.setInt(1, id);
            ps.setString(2, NombretxtField.getText());
            ps.setString(3, ApellidotxtField.getText());
            ps.setInt(4, edad);
            ps.setString(5, TelefonotxtField.getText());
            ps.setString(6, CarreratxtField.getText());

            if (ps.executeUpdate() > 0) {
                Lista.setModel(mod);
                mod.removeAllElements();
                mod.addElement("! Inserción Exitosa");
                IdtxtField.setText("");
                NombretxtField.setText("");
                ApellidotxtField.setText("");
                EdadtxtField.setText("");
                TelefonotxtField.setText("");
                CarreratxtField.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Por favor, ingresa un número válido para ID y Edad.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void modificar() throws SQLException {
        conectar();
        try {
            int id = Integer.parseInt(IdtxtField.getText());
            int edad = Integer.parseInt(EdadtxtField.getText());

            ps = con.prepareStatement("UPDATE estudiante SET nombre = ?, apellido = ?, edad = ?, telefono = ?, carrera = ? WHERE id = ?");
            ps.setString(1, NombretxtField.getText());
            ps.setString(2, ApellidotxtField.getText());
            ps.setInt(3, edad);
            ps.setString(4, TelefonotxtField.getText());
            ps.setString(5, CarreratxtField.getText());
            ps.setInt(6, id);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(this, "Registro modificado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                listar(); // Actualizar la lista después de modificar el registro
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún registro con el ID proporcionado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Por favor, ingresa un número válido para la Edad.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, " Realizando Operacion", "Acceso a la Base de datos", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void cerrarConexion() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                JOptionPane.showMessageDialog(this, "Conexión cerrada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void dispose() {
        super.dispose();
        try {
            cerrarConexion();
            JOptionPane.showMessageDialog(this, "Conexión cerrada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, "Error al cerrar la conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
