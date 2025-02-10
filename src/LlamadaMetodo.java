
public class LlamadaMetodo extends Instruccion {
    private Instruccion exp;
    private String metodo;
    private Instruccion[] params;

    public LlamadaMetodo(int linea, Instruccion exp, String metodo, Instruccion[] params) {
        super(linea);
        this.exp = exp;
        this.metodo = metodo;
        this.params = params;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        Objeto objParams[] = null;

        // Generamos el codigo necesario para obtener la expresión
        Objeto obj = exp.generarCodigo();

        // Generamos el codigo necesario para obtener cada parámetro
        if(params != null) {
            objParams = new Objeto[params.length];
            for(int i = 0; i < params.length; i++) {
                objParams[i] = params[i].generarCodigo();
            }
        }

        // Generamos el código del objeto de la expresión con el método y los parámetros
        return obj.generarCodigoMetodo(getLinea(), metodo, objParams);
    }
}
