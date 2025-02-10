public class SentRepeatJust extends Instruccion{
    private Instruccion i, rep;

    public SentRepeatJust(int linea, Instruccion i, Instruccion rep) {
        super(linea);
        this.i = i;
        this.rep = rep;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        String etqInicio = Objeto.nuevaEtiqueta();
        String etqFin = Objeto.nuevaEtiqueta();
        String cont = Objeto.nuevoNombre();
        Objeto repObj = rep.generarCodigo();
        Variable repDin = new Variable(Objeto.nuevoNombre(), repObj.getBloque(), false, TipoInt.instancia);
        PLXC.out.println(repDin.getID() + " = " + repObj.getID() + ";");

        PLXC.out.println(cont + " = 1;");
        PLXC.out.println(etqInicio + ":");
        i.generarCodigo();
        PLXC.out.println(cont + " = " + cont + " + 1;");
        PLXC.out.println("if (" + repDin.getID() + " < " + cont + ") goto " + etqFin + ";");
        PLXC.out.println("goto " + etqInicio + ";");
        PLXC.out.println(etqFin + ":");

        return null;
    }
    
}


