import java.text.ParseException;

public class TipoBool extends Tipo {
    public static TipoBool instancia = new TipoBool();

    private TipoBool() {
        super(Predefinidos.BOOL, 0, false);
    }

    @Override
    public Objeto generarCodigoMetodo(String metodo, Objeto[] param, int linea) throws Exception {
        return null;
    }

    @Override
    public Objeto generarCodigoInstancia(Instancia instancia, String metodo, Objeto[] param, int linea) throws Exception {
        Objeto obj;
        Tipo tipo;
        Variable var;
        Etiqueta etq;

        switch(metodo) {
            case Metodos.CREAR_VARIABLE:
                obj = param[0];
                
                if(!(obj instanceof Instancia)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", linea);
                }
                
                tipo = ((Instancia) obj).getTipo();
                if(tipo != this) {
                    throw new ParseException("<" + obj.getNombre() + "> no es de tipo " + getNombre(), linea);
                }

                PLXC.out.println(instancia.getIDC() + " = " + obj.getIDC() + ";");
                return param[0];
            // AND y OR (Mirar Clase CortoCircuito)
            // $t0 = a;
            // $t1 = $t0 (resultado);
            // if ($t1 == 0) / ($t1 == 1) goto L;
            // $t2 = b;
            // $t1 = $t2;
            // L:
            case Metodos.AND:
            case Metodos.OR:
                if(param == null) {
                    throw new ParseException("No se pasaron parámetros para " + metodo, linea);
                }

                etq = (Etiqueta) param[0];

                var = new Variable(newNombObj(), instancia.getBloque(), false, this);
                var.generarCodigoMetodo(Metodos.CREAR_VARIABLE, new Objeto[]{instancia}, linea); // $t1 = $t0

                switch(metodo) {
                    case Metodos.AND:
                        PLXC.out.println("if (" + var.getIDC() + " == 0) goto " + etq.getNombre() + ";");
                        break;
                    case Metodos.OR:
                        PLXC.out.println("if (" + var.getIDC() + " == 1) goto " + etq.getNombre() + ";");
                        break;
                }

                return var;
            case Metodos.NOT:
                var = new Variable(newNombObj(), instancia.getBloque(), false, this);
                PLXC.out.println(var.getIDC() + " = 1 - " + instancia.getIDC() + ";");
                return var;
            default:
                throw new ParseException("Método " + metodo + " no permitido para " + getNombre(), linea);
        }
    }
}
