public abstract class Tipo extends Objeto {
    public static final class Predefinidos {
        public static final String INT = "$int";
        public static final String CHAR = "$char";
        public static final String FLOAT = "$float";
        public static final String BOOL = "$bool";
        public static final String STRING = "$string";
    };
    
    public Tipo(String nombre, int bloque, boolean mutable) {
        super(nombre, bloque, mutable);
    }

    public boolean esArrayCompatible(Tipo tipo) {
        return this == tipo;
    }

    public abstract Objeto generarCodigoInstancia(int linea, Instancia instancia, String metodo, Objeto[] params) throws Exception;
}
