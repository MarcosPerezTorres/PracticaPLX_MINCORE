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

        Etiqueta etq = new Etiqueta(Objeto.nuevaEtiqueta(), objA.getBloque());

        Objeto result = objA.generarCodigoMetodo(getLinea(), metodo, new Objeto[]{etq}); // ($t1 = $t0; if($t0 == 0) goto L) -> $t1

        Objeto objB = b.generarCodigo(); // -> $t2

        result.generarCodigoMetodo(getLinea(), Metodos.CREAR_VARIABLE, new Objeto[]{objB}); // $t1 = $t2

        etq.generarCodigoMetodo(getLinea(), Metodos.PONER_ETQ, null); // L: ...

        return result;
    }
}
