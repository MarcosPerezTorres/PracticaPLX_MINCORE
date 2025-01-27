import java.text.ParseException;

public class ForAll extends Instruccion {
    private Variable var;
    private int left, right;
    private Instruccion exp, step;

    public ForAll(int linea, Variable var, int left, int right, Instruccion step, Instruccion exp) {
        super(linea);
        this.var = var;
        this.left = left;
        this.right = right;
        this.step = step;
        this.exp = exp;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        String etqForAll = Objeto.nuevaEtiqueta();
        String etqFalse = Objeto.nuevaEtiqueta();
        String etqFinal = Objeto.nuevaEtiqueta();
        
        Variable result = new Variable(Objeto.nuevoNombre(), var.getBloque(), false, TipoBool.instancia);
        PLXC.out.println(result.getID() + " = 1;"); // $t0 = 1;
        PLXC.out.println(var.getID() + " = " + left + ";"); // x = left;

        PLXC.out.println(etqForAll + ":"); // etqForAll:
        Objeto expObj = exp.generarCodigo();

        if(!(expObj instanceof Instancia)) {
            throw new ParseException("La expresión del forall debe ser una instancia (literal o variable)", getLinea());
        }

        if(((Instancia) expObj).getTipo() != TipoBool.instancia) {
            expObj = expObj.generarCodigoMetodo(getLinea(), Metodos.CAST, new Objeto[]{TipoBool.instancia});
        }

        Objeto stepObj = step.generarCodigo();

        PLXC.out.println("if (" + expObj.getID() + " == 0) goto " + etqFalse + ";"); // if (exp == 0) goto etqFalse
        PLXC.out.println(var.getID() + " = " + var.getID() + " + " + stepObj.getID() + ";"); // x = x + step;
        PLXC.out.println("if (" + right + " < " + var.getID() + ") goto " + etqFinal + ";"); // if (right < x) goto etqFinal
        PLXC.out.println("goto " + etqForAll + ";");

        PLXC.out.println(etqFalse + ":"); // etqFalse:
        PLXC.out.println(result.getID() + " = 0;"); // $t0 = 0;

        PLXC.out.println(etqFinal + ":"); // etqFinal:

        return result;
    }
    
}
