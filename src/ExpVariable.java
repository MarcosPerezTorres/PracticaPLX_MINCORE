public class ExpVariable extends Instruccion {
    private Variable v;

    public ExpVariable(int linea, Variable v) {
        super(linea);
        this.v = v;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        return v;
    }
}