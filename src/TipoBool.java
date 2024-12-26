public class TipoBool extends Tipo {
    public static TipoBool instancia = new TipoBool(Predefinidos.BOOL, 0, false);

    private TipoBool(String nombre, int bloque, boolean mutable) {
        super(nombre, bloque, mutable);
    }

    @Override
    public Objeto generarCodigoInstancia(Instancia instancia, String metodo, Objeto[] param, int linea) throws Exception {
        return null;
    }

    @Override
    public Objeto generarCodigoMetodo(String metodo, Objeto[] param, int linea) throws Exception {
        return null;
    }
}
