public class SentRepeat extends Instruccion {
    private Instruccion i, rep;

    public SentRepeat(int linea, Instruccion i, Instruccion rep) {
        super(linea);
        this.i = i;
        this.rep = rep;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        String etqInicio = Objeto.nuevaEtiqueta();
        String etqFin = Objeto.nuevaEtiqueta();

        String cont = Objeto.nuevoNombre();

        PLXC.out.println(cont + " = 1;");
        PLXC.out.println(etqInicio + ":");
        i.generarCodigo();
        PLXC.out.println(cont + " = " + cont + " + 1;");
        Objeto repObj = rep.generarCodigo();
        PLXC.out.println("if (" + repObj.getID() + " < " + cont + ") goto " + etqFin + ";");
        PLXC.out.println("goto " + etqInicio + ";");
        PLXC.out.println(etqFin + ":");

        return null;
    }
    
}
