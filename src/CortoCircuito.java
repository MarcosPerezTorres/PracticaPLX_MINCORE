public class CortoCircuito extends Instruccion {
    private Instruccion a, b;
    private String metodo;

    public CortoCircuito(int linea, Instruccion a, String metodo, Instruccion b) {
        super(linea);
        this.a = a;
        this.metodo = metodo;
        this.b = b;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        Objeto objA = a.generarCodigo(); // -> $t0

        Etiqueta etq = new Etiqueta(Objeto.newEtiqueta(), objA.getBloque());

        Objeto result = objA.generarCodigoMetodo(metodo, new Objeto[]{etq}, getLinea()); // ($t1 = $t0; if($t0 == 0) goto L) -> $t1

        Objeto objB = b.generarCodigo(); // -> $t2

        result.generarCodigoMetodo(Metodos.CREAR_VARIABLE, new Objeto[]{objB}, getLinea()); // $t1 = $t2

        etq.generarCodigoMetodo(Metodos.PONER_ETQ, null, getLinea()); // L: ...

        return result;
    }
}
