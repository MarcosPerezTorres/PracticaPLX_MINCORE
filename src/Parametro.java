public class Parametro {
    private Tipo tipo;
    private String nombre;

    public Parametro(Tipo tipo, String nombre){
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public Tipo getTipo() { return tipo; }
    public String getNombre() { return nombre; }
}
