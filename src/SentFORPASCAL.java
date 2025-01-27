import java.text.ParseException;

public class SentFORPASCAL extends Instruccion {
    private Instruccion asig, limite, step, cuerpo;
    private Variable var;
    boolean down;

    public SentFORPASCAL(int linea, Instruccion asig, Variable var, Instruccion limite, Instruccion step, Instruccion cuerpo, boolean down) {
        super(linea);
        this.asig = asig;
        this.var = var;
        this.limite = limite;
        this.step = step;
        this.cuerpo = cuerpo;
        this.down = down;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        String etqForPascal = Objeto.nuevaEtiqueta();
        String etqFinal = Objeto.nuevaEtiqueta();

        asig.generarCodigo();
        Objeto limiteObj = limite.generarCodigo();

        if(!(limiteObj instanceof Instancia)) {
            throw new ParseException("<" + limiteObj.getNombre() + "> no es una instancia (literal o variable)", getLinea());
        }

        if(((Instancia) limiteObj).getTipo() != TipoInt.instancia) {
            limiteObj = limiteObj.generarCodigoMetodo(getLinea(), Metodos.CAST, new Objeto[]{TipoInt.instancia});
        }

        PLXC.out.println(etqForPascal + ":"); // etqForPascal:
        if(down) {
            PLXC.out.println("if (" + var.getID() + " < " + limiteObj.getID() + ") goto " + etqFinal + ";");
        } else {
            PLXC.out.println("if (" + limiteObj.getID() + " < " + var.getID() + ") goto " + etqFinal + ";");
        }
        cuerpo.generarCodigo();
        Objeto stepObj = step.generarCodigo();
        if(down) {
            PLXC.out.println(var.getID() + " = " + var.getID() + " - " + stepObj.getID() + ";"); // var = var - step;
        } else {
            PLXC.out.println(var.getID() + " = " + var.getID() + " + " + stepObj.getID() + ";"); // var = var + step;
        }
        PLXC.out.println("goto " + etqForPascal + ";"); // goto etqForPascal;

        PLXC.out.println(etqFinal + ":"); // etqFinal:

        return null;
    }
    
}
