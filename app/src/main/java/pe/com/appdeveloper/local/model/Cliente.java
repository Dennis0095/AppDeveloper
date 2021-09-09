package pe.com.appdeveloper.local.model;

public class Cliente {
    private String nombre;
    private String apellido;
    private String edad;
    private String fecNac;

    public Cliente(String nombre_, String apellido_, String edad_, String fecNac_) {
        nombre = nombre_;
        apellido = apellido_;
        edad = edad_;
        fecNac = fecNac_;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEdad() {
        return edad;
    }

    public String getFecNac() {
        return fecNac;
    }
}
