/*Clase alumno */
public class Alumno {

    // Atributos (privados: solo se acceden con los getters)
    private String nombre;
    private String apellido;
    private String nivel;

    // Constructor: se usa al crear un Alumno nuevo
    public Alumno(String nombre, String apellido, String nivel) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.nivel = nivel;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNivel() {
        return nivel;
    }
}
