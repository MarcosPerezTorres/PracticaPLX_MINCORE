public class Literal extends Instancia {
    private Object valor;

    public Literal(int bloque, Tipo tipo, Object valor) {
        super(nuevoNombre(), bloque, false, tipo);
        this.valor = valor;
    }

    public Object getValor() { return this.valor; }

    @Override
    public Objeto generarCodigoMetodo(int linea, String metodo, Objeto[] params) throws Exception {
        return getTipo().generarCodigoInstancia(linea, this, metodo, params);
    }
}
