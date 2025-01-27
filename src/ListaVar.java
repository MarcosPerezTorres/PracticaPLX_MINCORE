public class ListaVar extends Bloque {
    private Tipo tipo;

    public ListaVar(int linea, Tipo tipo) {
        super(linea);
        this.tipo = tipo;
    }

    public Tipo getTipo() { return this.tipo; }
}
