public class SentSelect extends Instruccion {
    private Variable var;
    private Instruccion from, to, step, df, cuerpo;
    private String etqInicio, etqDefault, etqFinal, etqInicioAnt, etqDefaultSig;
    private boolean first;

    public SentSelect(int linea, Variable var, boolean first, Instruccion from, Instruccion to, Instruccion step, Instruccion df) {
        super(linea);
        this.var = var;
        this.first = first;
        this.from = from;
        this.to = to;
        this.step = step;
        this.df = df;
        this.cuerpo = null;

        this.etqInicio = Objeto.nuevaEtiqueta();
        this.etqDefault = Objeto.nuevaEtiqueta();
    }

    public void setCuerpo(Instruccion i) { this.cuerpo = i; }

    public void setEtqFinal(String etq) { this.etqFinal = etq; }

    public void setEtqInicioAnt(String etq) { this.etqInicioAnt = etq; }

    public void setEtqDefaultSig(String etq) { this.etqDefaultSig = etq; }

    public String getEtqDefault() { return this.etqDefault; }

    public String getEtqInicio() { return this.etqInicio; }

    @Override
    public Objeto generarCodigo() throws Exception {
        Objeto fromObj = from.generarCodigo();
        Objeto toObj = to.generarCodigo();
        Objeto stepObj = step.generarCodigo();
        Objeto dfObj = df.generarCodigo();

        // Valor inicial
        if(first) {
            PLXC.out.println(var.getID() + " = " + fromObj.getID() + " - " + stepObj.getID() + ";");
        } else {
            PLXC.out.println(var.getID() + " = " + toObj.getID() + " + " + stepObj.getID() + ";");
        }

        PLXC.out.println(etqInicio + ":");
        if(first) {
            PLXC.out.println(var.getID() + " = " + var.getID() + " + " + stepObj.getID() + ";");
        } else {
            PLXC.out.println(var.getID() + " = " + var.getID() + " - " + stepObj.getID() + ";");
        }
        if(first) {
            if(etqInicioAnt == null) {
                PLXC.out.println("if (" + toObj.getID() + " < " + var.getID() + ") goto " + etqDefault + ";"); 
            } else {
                PLXC.out.println("if (" + toObj.getID() + " < " + var.getID() + ") goto " + etqInicioAnt + ";");
            }
        } else {
            if(etqInicioAnt == null) {
                PLXC.out.println("if (" + var.getID() + " < " + fromObj.getID() + ") goto " + etqDefault + ";"); 
            } else {
                PLXC.out.println("if (" + var.getID() + " < " + fromObj.getID() + ") goto " + etqInicioAnt + ";");
            }
        }
        Objeto cuerpoObj = cuerpo.generarCodigo();
        if(cuerpoObj != null) {
            // El cuerpo es una expresion booleana
            PLXC.out.println("if (" + cuerpoObj.getID() + " == 1) goto " + etqFinal + ";");
        }

        PLXC.out.println("goto " + etqInicio + ";");

        PLXC.out.println(etqDefault + ":");
        PLXC.out.println(var.getID() + " = " + dfObj.getID() + ";");
        if(etqDefaultSig == null) {
            PLXC.out.println("goto " + etqFinal + ";");
        } else {
            PLXC.out.println("goto " + etqDefaultSig + ";");
        }

        if(etqInicioAnt == null) PLXC.out.println(etqFinal + ":"); // Soy el primer select de todos los anidados

        return null;
    }
}
