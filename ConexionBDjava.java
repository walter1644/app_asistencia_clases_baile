import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBDjava {

    public static Connection conectar() throws SQLException, IOException {
        Properties config = new Properties();

        // 1) Cargar el archivo config.properties
        try (FileInputStream archivo = new FileInputStream("config.properties")) {
            config.load(archivo);
        }

        // 2) Leer los datos de conexión
        String url      = config.getProperty("db.url");
        String usuario  = config.getProperty("db.user");
        String password = config.getProperty("db.password");

        // 3) Abrir y devolver la conexión
        return DriverManager.getConnection(url, usuario, password);
    }
}