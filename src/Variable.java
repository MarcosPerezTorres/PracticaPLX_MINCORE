public class Variable extends Instancia {
    public Variable(String nombre, int bloque, boolean mutable, Tipo tipo) {
        super(nombre, bloque, mutable, tipo);
    }

    @Override
    public Objeto generarCodigoMetodo(int linea, String metodo, Objeto[] params) throws Exception {
        return getTipo().generarCodigoInstancia(linea, this, metodo, params);
    }
}
