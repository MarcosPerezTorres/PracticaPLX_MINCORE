public class Elvis extends Instruccion {
    private Instruccion exp1;
    private Instruccion exp2;

    public Elvis(int linea, Instruccion exp1, Instruccion exp2) {
        super(linea);
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        Objeto exp1Obj = exp1.generarCodigo();
        Objeto exp2Obj = exp2.generarCodigo();

        String etq0 = Objeto.nuevaEtiqueta();
        String etqFin = Objeto.nuevaEtiqueta();
        
        Variable temp = new Variable(Objeto.nuevoNombre(), 0, false, TipoInt.instancia);

        // Se evalúa exp1, si es distinto de 0, se usa exp1
        PLXC.out.println(temp.getID() + " = " + exp1Obj.getID() + ";");
        PLXC.out.println("if (" + temp.getID() + " != 0) goto " + etqFin + ";");

        // Si exp1 es 0, se evalúa exp2
        PLXC.out.println(etq0 + ":");
        PLXC.out.println(temp.getID() + " = " + exp2Obj.getID() + ";");

        PLXC.out.println(etqFin + ":");
        return temp;
    }
}