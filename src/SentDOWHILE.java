import java.text.ParseException;

public class SentDOWHILE extends Instruccion {
    Instruccion exp, i;

    public SentDOWHILE(int linea, Instruccion exp, Instruccion i) {
        super(linea);
        this.exp = exp;
        this.i = i;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        Objeto expObj;
        String etq = Objeto.newEtiqueta();

        PLXC.out.println(etq + ":"); // etq:
        i.generarCodigo(); // i

        expObj = exp.generarCodigo();

        if(!(expObj instanceof Instancia)) {
            throw new ParseException("La expresión del do-while debe ser una instancia (literal o variable)", getLinea());
        }

        // Intentamos convertir a booleano
        if(((Instancia) expObj).getTipo() != TipoBool.instancia) {
            expObj = expObj.generarCodigoMetodo(Metodos.CAST, new Objeto[]{TipoBool.instancia}, getLinea());
        }

        PLXC.out.println("if (" + expObj.getIDC() + " != 0) goto " + etq + ";"); // if (exp != 0) goto etq;

        return null;
    }
    
}