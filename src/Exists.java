import java.text.ParseException;

public class Exists extends Instruccion {
    private Variable var;
    private int left,right;
    private Instruccion exp, step;

    public Exists(int linea, Variable var, int left, int right, Instruccion step, Instruccion exp){
        super(linea);
        this.var = var;
        this.left = left;
        this.right = right;
        this.step = step;
        this.exp = exp;
    }


    @Override
    public Objeto generarCodigo() throws Exception {
        String etqExists = Objeto.nuevaEtiqueta();
        String etqTrue = Objeto.nuevaEtiqueta();
        String etqFinal = Objeto.nuevaEtiqueta();

        Variable result = new Variable(Objeto.nuevoNombre(), var.getBloque(), true, TipoBool.instancia);
        PLXC.out.println(result.getID() + " = 0 " + ";");
        PLXC.out.println(var.getID() + " = " + left + ";");

        PLXC.out.println(etqExists + ":");
        Objeto expObj = exp.generarCodigo();

        if(!(expObj instanceof Instancia)){
            throw new ParseException("La expresión del exists debe ser una instancia (literal o variable)", getLinea());
        }

        if(((Instancia) expObj).getTipo() != TipoBool.instancia){
            expObj = expObj.generarCodigoMetodo(getLinea(), Metodos.CAST, null);
        }

        Objeto stepObj = step.generarCodigo();

        PLXC.out.println("if (" + expObj.getID() + " == 1) goto " +  etqTrue + ";");  //if (exp == 1) goto etqTrue
        PLXC.out.println(var.getID() + " = " + var.getID() + " + " + stepObj.getID() + ";");  // x = x + step
        PLXC.out.println("if (" + right + " < " + var.getID() + ") goto " + etqFinal + ";");  // if (right < x) goto etqFinal
        PLXC.out.println("goto " + etqExists + ";");

        PLXC.out.println(etqTrue + ":");
        PLXC.out.println(result.getID() + " = 1;");

        PLXC.out.println(etqFinal + ":");
        
        return result;
    }
}