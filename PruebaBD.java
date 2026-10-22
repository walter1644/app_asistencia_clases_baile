import java.sql.Connection;

public class PruebaBD {
    public static void main(String[] args) {
        try (Connection con = ConexionBDjava.conectar()) {
            System.out.println("¡Conectado a Aiven!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}