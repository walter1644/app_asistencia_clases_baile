import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana principal para registrar la asistencia a las clases de salsa.
 *
 * Flujo: se escribe nombre y apellido, se elige el nivel y se aprieta
 * "Registrar asistencia". La persona se guarda en la base (Aiven)
 * y aparece en la tabla.
 */
public class VentanaAsistencia extends JFrame {

    // ---------- Datos ----------
    private ArrayList<Alumno> listaAlumnos = new ArrayList<>();

    // Los 6 niveles de las clases
    private String[] niveles = {
        "Inicial", "Principiantes", "Pre-intermedio",
        "Intermedio", "Pre-avanzado", "Avanzado"
    };

    // ---------- Componentes de la interfaz ----------
    private JTextField txtNombre = new JTextField(15);
    private JTextField txtApellido = new JTextField(15);
    private JComboBox<String> cmbNivel = new JComboBox<>(niveles);
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JLabel lblTotal = new JLabel("Total de asistentes: 0");

    // ---------- Constructor: arma toda la ventana ----------
    public VentanaAsistencia() {
        setTitle("Asistencia - Clases de Salsa");
        setSize(900, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);

        cargarAlumnos();   // NUEVO: trae de la base los alumnos ya guardados
    }

    // ---------- Parte de arriba (norte): formulario ----------
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        panel.add(new JLabel("  Nombre:"));
        panel.add(txtNombre);

        panel.add(new JLabel("  Apellido:"));
        panel.add(txtApellido);

        panel.add(new JLabel("  Nivel de la clase:"));
        panel.add(cmbNivel);

        JButton btnRegistrar = new JButton("Registrar asistencia");
        btnRegistrar.addActionListener(e -> registrarAsistencia());
        panel.add(new JLabel(""));
        panel.add(btnRegistrar);

        return panel;
    }

    // ---------- Centro: tabla con los asistentes ----------
    private JScrollPane crearTabla() {
        String[] columnas = {"Nombre", "Apellido", "Nivel"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        return new JScrollPane(tabla);
    }

    // ---------- Parte de abajo: total y botón eliminar ----------
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnEliminar = new JButton("Eliminar seleccionado");
        btnEliminar.addActionListener(e -> eliminarSeleccionado());

        panel.add(lblTotal);
        panel.add(btnEliminar);
        return panel;
    }

    // ---------- NUEVO: cargar los alumnos guardados en la base ----------
    private void cargarAlumnos() {
        try {
            for (Alumno a : AlumnoDAO.listar()) {
                listaAlumnos.add(a);
                modeloTabla.addRow(new Object[]{
                    a.getNombre(), a.getApellido(), a.getNivel()
                });
            }
            actualizarTotal();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los alumnos: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- Acción: registrar asistencia ----------
    private void registrarAsistencia() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String nivel = (String) cmbNivel.getSelectedItem();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Completá el nombre y el apellido.",
                    "Faltan datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 1) Guardar en la base (si falla, salta al catch y no se agrega nada)
            AlumnoDAO.insertar(nombre, apellido, nivel);

            // 2) Recién ahora lo agregamos a la lista y a la tabla
            Alumno alumno = new Alumno(nombre, apellido, nivel);
            listaAlumnos.add(alumno);
            modeloTabla.addRow(new Object[]{
                alumno.getNombre(), alumno.getApellido(), alumno.getNivel()
            });

            // 3) Limpiar los campos y actualizar el total
            txtNombre.setText("");
            txtApellido.setText("");
            txtNombre.requestFocus();
            actualizarTotal();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- Acción: eliminar la fila seleccionada ----------
    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccioná una fila de la tabla primero.",
                    "Nada seleccionado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Alumno alumno = listaAlumnos.get(fila);

            // 1) Borrar de la base
            AlumnoDAO.eliminar(alumno.getNombre(), alumno.getApellido());

            // 2) Si salió bien, sacarlo de la lista y de la tabla
            listaAlumnos.remove(fila);
            modeloTabla.removeRow(fila);
            actualizarTotal();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- Actualiza el texto del total ----------
    private void actualizarTotal() {
        lblTotal.setText("Total de asistentes: " + listaAlumnos.size());
    }

    // ---------- main: punto de entrada del programa ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaAsistencia().setVisible(true);
        });
    }
}