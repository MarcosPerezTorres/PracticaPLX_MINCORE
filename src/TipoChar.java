public class TipoChar extends Tipo {
    public static TipoChar instancia = new TipoChar();

    private TipoChar() {
        super(Predefinidos.CARACTER, 0, false);
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
