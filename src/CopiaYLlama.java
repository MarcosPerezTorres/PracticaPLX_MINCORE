public class CopiaYLlama extends Instruccion {
    private Instruccion exp;
    private String metodo;
    private Instruccion[] params;

    public CopiaYLlama(int linea, Instruccion exp, String metodo, Instruccion[] params) {
        super(linea);
        this.exp = exp;
        this.metodo = metodo;
        this.params = params;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        Objeto objParams[] = null;
        Objeto copia;

        // Creamos variable
        Variable obj = (Variable) exp.generarCodigo();

        // La copiamos en otra
        copia = new Variable(Objeto.nuevoNombre(), obj.getBloque(), obj.esMutable(), obj.getTipo());
        copia.generarCodigoMetodo(getLinea(), Metodos.CREAR_VARIABLE, new Objeto[]{obj});

        if(params != null) {
            objParams = new Objeto[params.length];
            for(int i = 0; i < params.length; i++) {
                objParams[i] = params[i].generarCodigo();
            }
        }

        // Llamamos método
        obj.generarCodigoMetodo(getLinea(), metodo, objParams);

        // Devolvemos el valor que tenía la variable antes
        return copia;
    }
}
