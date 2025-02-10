public class Ternario extends Instruccion {
    private Instruccion condicion;
    private Instruccion expTrue;
    private Instruccion expFalse;

    public Ternario(int linea, Instruccion condicion, Instruccion expTrue, Instruccion expFalse) {
        super(linea);
        this.condicion = condicion;
        this.expTrue = expTrue;
        this.expFalse = expFalse;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        Objeto condObj = condicion.generarCodigo();
        String etqTrue = Objeto.nuevaEtiqueta();
        String etqFalse = Objeto.nuevaEtiqueta();
        String etqFin = Objeto.nuevaEtiqueta();
        Variable temp = new Variable(Objeto.nuevoNombre(), 0, false, TipoInt.instancia);
        PLXC.out.println("if (" + condObj.getID() + " != 0) goto " + etqTrue + ";");
        PLXC.out.println("goto " + etqFalse + ";");

        // Rama verdadera:
        PLXC.out.println(etqTrue + ":");
        Objeto trueObj = expTrue.generarCodigo();
        PLXC.out.println(temp.getID() + " = " + trueObj.getID() + ";");
        PLXC.out.println("goto " + etqFin + ";");

        // Rama falsa:
        PLXC.out.println(etqFalse + ":");
        Objeto falseObj = expFalse.generarCodigo();
        PLXC.out.println(temp.getID() + " = " + falseObj.getID() + ";");

        PLXC.out.println(etqFin + ":");
        return temp;
    }
}
