import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {

    public static void insertar(String nombre, String apellido, String nivel) throws Exception {
        String sql = "INSERT INTO alumnos (nombre, apellido, nivel) VALUES (?, ?, ?)";

        try (Connection con = ConexionBDjava.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, nivel);

            ps.executeUpdate();
        }
    }
    public static void eliminar(String nombre, String apellido) throws Exception {
        String sql = "DELETE FROM alumnos WHERE nombre = ? AND apellido = ?";

        try (Connection con = ConexionBDjava.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, apellido);

            ps.executeUpdate();
        }
    }



public static List<Alumno> listar() throws Exception {
    List<Alumno> alumnos = new ArrayList<>();
    String sql = "SELECT nombre, apellido, nivel FROM alumnos";

    try (Connection con = ConexionBDjava.conectar();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String nombre   = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            String nivel    = rs.getString("nivel");

            Alumno a = new Alumno(nombre, apellido, nivel);
            alumnos.add(a);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return alumnos;
}
}