public class ExpTipo extends Instruccion {
    private Tipo t;

    public ExpTipo(int linea, Tipo t) {
        super(linea);
        this.t = t;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        return t;
    }
    
}