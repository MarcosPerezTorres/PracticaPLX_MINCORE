import java.text.ParseException;

public class SentWHILE extends Instruccion {
    private Instruccion exp, i;

    public SentWHILE(int linea, Instruccion exp, Instruccion i) {
        super(linea);
        this.exp = exp;
        this.i = i;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        Objeto expObj;
        String etqComienzo = Objeto.nuevaEtiqueta();
        String etqFinal = Objeto.nuevaEtiqueta();

        PLXC.out.println(etqComienzo + ":"); // etqComienzo:
        
        expObj = exp.generarCodigo();

        if(!(expObj instanceof Instancia)) {
            throw new ParseException("La expresión del while debe ser una instancia (literal o variable)", getLinea());
        }

        // Intentamos convertirlo a booleano
        if(((Instancia) expObj).getTipo() != TipoBool.instancia) {
            expObj = expObj.generarCodigoMetodo(getLinea(), Metodos.CAST, new Objeto[]{TipoBool.instancia});
        }

        PLXC.out.println("if (" + expObj.getID() + " == 0) goto " + etqFinal + ";"); // if (exp == 0) goto etqFinal
        
        i.generarCodigo();
        PLXC.out.println("goto " + etqComienzo + ";"); // goto etqComienzo
        
        PLXC.out.println(etqFinal + ":"); // etqFinal:

        return null;
    }
    
}
