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
 * "Registrar asistencia". La persona aparece en la tabla.
 */
public class VentanaAsistencia extends JFrame {

    // ---------- Datos ----------
    // “Una variable privada llamada listaAlumnos que solo guarda objetos de tipo Alumno, le asigno una lista nueva y vacía.”
    private ArrayList<Alumno> listaAlumnos = new ArrayList<>();

    // Los 6 niveles de las clases
    private String[] niveles = {
        "Inicial", "Principiantes", "Pre-intermedio",
        "Intermedio", "Pre-avanzado", "Avanzado"
    };


    // ---------- Componentes de la interfaz ----------
    private JTextField txtNombre = new JTextField(15); //renglon para anotar el nombre
    private JTextField txtApellido = new JTextField(15); //renglon para anotar el apellido
    private JComboBox<String> cmbNivel = new JComboBox<>(niveles); //ACTIVA LA LISTA DESPLEGABLE DE LOS NIVELES
    private DefaultTableModel modeloTabla;   // guarda las filas de la tabla
    private JTable tabla;                    // muestra las filas en pantalla
    private JLabel lblTotal = new JLabel("Total de asistentes: 0");





    // ---------- Constructor: arma toda la ventana ----------
    public VentanaAsistencia() {
        setTitle("Asistencia - Clases de Salsa");// título de la ventana
        setSize(900, 800); // tamaño de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // cerrar la ventana termina el programa
        setLocationRelativeTo(null);              // centrara la ventana en la pantalla
        setLayout(new BorderLayout(10, 10));      // 5 zonas: NORTH, CENTER, SOUTH...

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }


    // ---------- Metodo de la parte de arriba (norte): formulario ----------
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5)); // 4 filas, 2 columnas

        panel.add(new JLabel("  Nombre:")); //fila 1
        panel.add(txtNombre);

        panel.add(new JLabel("  Apellido:")); //fila 2
        panel.add(txtApellido);

        panel.add(new JLabel("  Nivel de la clase:")); //fila 3
        panel.add(cmbNivel);

        // Botón: cuando se hace clic, se ejecuta registrarAsistencia()
        JButton btnRegistrar = new JButton("Registrar asistencia"); //fila 4
        btnRegistrar.addActionListener(e -> registrarAsistencia());
        panel.add(new JLabel(""));   // celda vacía para alinear el botón a la derecha
        panel.add(btnRegistrar);

        return panel;
    }






    // ---------- Centro: tabla con los asistentes ----------
    private JScrollPane crearTabla() {
        String[] columnas = {"Nombre", "Apellido", "Nivel"};
        modeloTabla = new DefaultTableModel(columnas, 0); // 0 = empieza sin filas
        tabla = new JTable(modeloTabla);
        return new JScrollPane(tabla);  // JScrollPane agrega la barra de desplazamiento (solo se va a ver si hay muchas filas)
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







    // ---------- Acción: registrar asistencia ----------
    private void registrarAsistencia() {
        // 1) Leer lo que escribió la persona (trim() borra espacios de más)
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String nivel = (String) cmbNivel.getSelectedItem();

        // 2) Validar que no haya campos vacíos
        if (nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Completá el nombre y el apellido.",
                    "Faltan datos", JOptionPane.WARNING_MESSAGE);
            return;  // salimos del método sin registrar
        }

        // 3) Crear el objeto Alumno y guardarlo en la lista
        Alumno alumno = new Alumno(nombre, apellido, nivel);
        listaAlumnos.add(alumno);

        // 4) Agregar una fila a la tabla para que se vea en pantalla
        modeloTabla.addRow(new Object[]{
            alumno.getNombre(), alumno.getApellido(), alumno.getNivel()
        });

        // 5) Limpiar los campos y actualizar el total
        txtNombre.setText("");
        txtApellido.setText("");
        txtNombre.requestFocus();   // el cursor vuelve al campo Nombre
        actualizarTotal();
    }

    // ---------- Acción: eliminar la fila seleccionada ----------
    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();   // -1 significa "no hay nada seleccionado"

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccioná una fila de la tabla primero.",
                    "Nada seleccionado", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        listaAlumnos.remove(fila);      // lo sacamos de la lista
        modeloTabla.removeRow(fila);    // y de la tabla
        actualizarTotal();
    }

    // ---------- Actualiza el texto del total ----------
    private void actualizarTotal() {
        lblTotal.setText("Total de asistentes: " + listaAlumnos.size());
    }

    // ---------- main: punto de entrada del programa ----------
    public static void main(String[] args) {
        // Forma recomendada de abrir una ventana Swing
        SwingUtilities.invokeLater(() -> {
            new VentanaAsistencia().setVisible(true);
        });
    }
}
