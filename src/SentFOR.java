import java.text.ParseException;

public class SentFOR extends Instruccion {
    private Instruccion inicio, cond, act, cuerpo;

    public SentFOR(int linea, Instruccion inicio, Instruccion cond, Instruccion act, Instruccion cuerpo) {
        super(linea);
        this.inicio = inicio;
        this.cond = cond;
        this.act = act;
        this.cuerpo = cuerpo;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        Objeto condObj;
        String etqFor = Objeto.nuevaEtiqueta();
        String etqFin = Objeto.nuevaEtiqueta();

        if(inicio != null) inicio.generarCodigo();

        PLXC.out.println(etqFor + ":"); // etqFor:

        if(cond != null) {
            condObj = cond.generarCodigo();

            if(!(condObj instanceof Instancia)) {
                throw new ParseException("La condición del for debe ser una instancia (literal o variable)", getLinea());
            }

            // Intentamos convertirlo a booleano
            if(((Instancia) condObj).getTipo() != TipoBool.instancia) {
                condObj = condObj.generarCodigoMetodo(getLinea(), Metodos.CAST, new Objeto[]{TipoBool.instancia});
            }

            PLXC.out.println("if (" + condObj.getID() + " == 0) goto " + etqFin + ";"); // if (exp == 0) goto etqFin
        }
        
        if(cuerpo != null) cuerpo.generarCodigo();
        if(act != null) act.generarCodigo();
        PLXC.out.println("goto " + etqFor + ";"); // goto etqFor

        PLXC.out.println(etqFin + ":"); // etqFin:

        return null;
    }
    
}
