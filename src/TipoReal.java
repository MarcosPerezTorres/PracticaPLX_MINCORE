public class TipoReal extends Tipo {
    public static TipoReal instancia = new TipoReal();

    private TipoReal() {
        super(Predefinidos.REAL, 0, false);
    }

    @Override
    public Objeto generarCodigoMetodo(String metodo, Objeto[] param, int linea) throws Exception {
        return null;
    }

    @Override
    public Objeto generarCodigoInstancia(Instancia instancia, String metodo, Objeto[] param, int linea) throws Exception {
        return null;
    }
}
