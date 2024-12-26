import java.text.ParseException;

public class SentIF extends Instruccion {
    private Instruccion exp, iTrue, iFalse;

    public SentIF(int linea, Instruccion exp, Instruccion iTrue, Instruccion iFalse) {
        super(linea);
        this.exp = exp;
        this.iTrue = iTrue;
        this.iFalse = iFalse;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        Objeto expObj = exp.generarCodigo();
        String etqFalse = Objeto.newEtiqueta();
        String etqFinal = Objeto.newEtiqueta();

        if(!(expObj instanceof Instancia)) {
            throw new ParseException("La expresión del if debe ser una instancia (literal o variable)", getLinea());
        }

        // Intentamos convertirlo a booleano
        if(((Instancia) expObj).getTipo() != TipoBool.instancia) {
            expObj = expObj.generarCodigoMetodo(Metodos.CAST, new Objeto[]{TipoBool.instancia}, getLinea());
        }

        PLXC.out.println("if (" + expObj.getIDC() + " == 0) goto " + etqFalse + ";"); // if (exp == 0) goto etqFalse;
        
        iTrue.generarCodigo();
        PLXC.out.println("goto " + etqFinal + ";"); // goto etqFinal;

        PLXC.out.println(etqFalse + ":");
        if(iFalse != null) iFalse.generarCodigo();

        PLXC.out.println(etqFinal + ":");

        return null;
    }
    
}