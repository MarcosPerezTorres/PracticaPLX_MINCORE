import java.text.ParseException;

public class TipoBool extends Tipo {
    public static TipoBool instancia = new TipoBool();

    private final String TRUE = "true";
    private final String FALSE = "false";

    private TipoBool() {
        super(Predefinidos.BOOL, 0, false);
    }

    @Override
    public Objeto generarCodigoMetodo(int linea, String metodo, Objeto[] params) throws Exception {
        return null;
    }

    @Override
    public Objeto generarCodigoInstancia(int linea, Instancia instancia, String metodo, Objeto[] params) throws Exception {
        Objeto obj;
        Tipo tipo;
        Variable var;
        Etiqueta etq;

        switch(metodo) {
            case Metodos.MOSTRAR:
                // if (a == 0) write "false"
                // else write "true"
                String etqFinal = nuevaEtiqueta();
                String etqFalse = nuevaEtiqueta();

                PLXC.out.println("if (" + instancia.getID() + " == 0) goto " + etqFalse + ";");

                for(int i = 0; i < TRUE.length(); i++) {
                    PLXC.out.println("writec " + Character.codePointAt(TRUE, i) + ";");
                }
                PLXC.out.println("goto " + etqFinal + ";");

                PLXC.out.println(etqFalse + ":");
                for(int i = 0; i < FALSE.length(); i++) {
                    PLXC.out.println("writec " + Character.codePointAt(FALSE, i) + ";");
                }

                PLXC.out.println(etqFinal + ":");
                break;
            case Metodos.ASIGNAR: // a = ....
                if(!instancia.esMutable()) {
                    throw new ParseException("No se pudo reasignar un valor a la constante <" + instancia.getNombre() + ">", linea);
                }
            case Metodos.CREAR_VARIABLE: // boolean a = ....
                obj = params[0];
                
                if(!(obj instanceof Instancia)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", linea);
                }
                
                tipo = ((Instancia) obj).getTipo();
                if(tipo != this) {
                    throw new ParseException("<" + obj.getNombre() + "> no es de tipo " + getNombre(), linea);
                }

                PLXC.out.println(instancia.getID() + " = " + obj.getID() + ";");
                return params[0];
            case Metodos.CREAR_LITERAL:
                PLXC.out.println(instancia.getID() + " = " + ((Literal) instancia).getValor() + ";");
                return instancia;
            // AND y OR (Mirar Clase CortoCircuito)
            // $t0 = a;
            // $t1 = $t0 (resultado);
            // if ($t1 == 0) / ($t1 == 1) goto L;
            // $t2 = b;
            // $t1 = $t2;
            // L:
            case Metodos.AND:
            case Metodos.OR:
                if(params == null) {
                    throw new ParseException("No se pasaron parámetros para " + metodo, linea);
                }

                etq = (Etiqueta) params[0];

                var = new Variable(nuevoNombre(), instancia.getBloque(), false, this);
                var.generarCodigoMetodo(linea, Metodos.CREAR_VARIABLE, new Objeto[]{instancia}); // $t1 = $t0

                switch(metodo) {
                    case Metodos.AND:
                        PLXC.out.println("if (" + var.getID() + " == 0) goto " + etq.getNombre() + ";");
                        break;
                    case Metodos.OR:
                        PLXC.out.println("if (" + var.getID() + " == 1) goto " + etq.getNombre() + ";");
                        break;
                }

                return var;
            case Metodos.NOT:
                var = new Variable(nuevoNombre(), instancia.getBloque(), false, this);
                PLXC.out.println(var.getID() + " = 1 - " + instancia.getID() + ";");
                return var;
                
            case Metodos.CAST:
                if(!(params[0] instanceof Tipo)) {
                    throw new ParseException(params[0].getNombre() + " no es un tipo", linea);
                }
                switch(params[0].getNombre()) {
                    case Predefinidos.INT:
                        // Se crea una variable temporal de tipo int.
                        var = new Variable(nuevoNombre(), instancia.getBloque(), false, (Tipo) params[0]);
                        // Como en el .flex se asigna 1 o 0, solo copiamos ese valor:
                        PLXC.out.println(var.getID() + " = " + instancia.getID() + ";");
                        return var;
                    case Predefinidos.BOOL:
                        return instancia;
                    default:
                        throw new ParseException("No se puede convertir un " + getNombre() + " a " + params[0].getNombre(), linea);
                }
            default:
                throw new ParseException("Método " + metodo + " no permitido para " + getNombre(), linea);
        }

        return null;
    }
}
