import java.text.ParseException;

public class TipoFloat extends Tipo {
    public static TipoFloat instancia = new TipoFloat();

    private TipoFloat() {
        super(Predefinidos.FLOAT, 0, false);
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
        String etq;
        
        switch(metodo) {
            case Metodos.MOSTRAR:
                if(instancia instanceof VariableArray) {
                    var = new Variable(nuevoNombre(), instancia.getBloque(), false, this);
                    PLXC.out.println(var.getID() + " = " + instancia.getID() + ";");
                    PLXC.out.println("print " + var.getID() + ";");
                } else {
                    PLXC.out.println("print " + instancia.getID() + ";");
                }

                break;
            case Metodos.ASIGNAR: // a = ....
                if(!instancia.esMutable()) {
                    throw new ParseException("No se puede reasignar un valor a la constante <" + instancia.getNombre() + ">", linea);
                }
            case Metodos.CREAR_VARIABLE: // float a = ....
                obj = params[0];
                
                if(!(obj instanceof Instancia)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", linea);
                }
                
                tipo = ((Instancia) obj).getTipo();
                if(tipo != this) {
                    // Intento convertir a real
                    obj = obj.generarCodigoMetodo(linea, Metodos.CAST, new Objeto[]{this});
                }

                PLXC.out.println(instancia.getID() + " = " + obj.getID() + ";");
                return params[0];
            case Metodos.CREAR_LITERAL:
                PLXC.out.println(instancia.getID() + " = " + ((Literal) instancia).getValor() + ";");
                return instancia;
            case Metodos.CAST:
                if(!(params[0] instanceof Tipo)) {
                    throw new ParseException(params[0].getNombre() + " no es un tipo", linea);
                }

                switch(params[0].getNombre()) {
                    case Predefinidos.FLOAT:
                        return instancia;
                    case Predefinidos.INT:
                        var = new Variable(nuevoNombre(), instancia.getBloque(), false, (Tipo) params[0]);
                        PLXC.out.println(var.getID() + " = (int) " + instancia.getID() + ";");
                        return var;
                    case Predefinidos.BOOL:
                        var = new Variable(nuevoNombre(), instancia.getBloque(), false, (Tipo) params[0]);
                        etq = nuevaEtiqueta();
                        PLXC.out.println(var.getID() + " = 1;"); // $t0 = 1
                        PLXC.out.println("if (" + instancia.getNombre() + " != 0) goto " + etq + ";"); // if(a != 0) goto L
                        PLXC.out.println(var.getID() + " = 0;"); // $t0 = 0
                        PLXC.out.println(etq + ":"); // L:

                        return var;
                    default:
                        throw new ParseException("No se puede convertir un " + getNombre() + " a " + params[0].getNombre(), linea);
                }
            // Operaciones
            case Metodos.SUMA:
            case Metodos.RESTA:
            case Metodos.PRODUCTO:
            case Metodos.DIVISION:
                if(params == null) {
                    throw new ParseException("No se han pasado parámetros para " + metodo, linea);
                }

                obj = params[0];

                if(!(obj instanceof Instancia)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", linea);
                }

                tipo = ((Instancia) obj).getTipo();
                if(tipo != this) {
                    obj = obj.generarCodigoMetodo(linea, Metodos.CAST, new Objeto[]{this});
                }

                if(obj instanceof VariableArray) {
                    // Necesito copiarlo en una variable (por la sintaxis que tiene CTD)
                    var = new Variable(nuevoNombre(), instancia.getBloque(), false, tipo);
                    PLXC.out.println(var.getID() + " = " + obj.getID() + ";");
                    obj = var;
                }

                var = new Variable(nuevoNombre(), instancia.getBloque(), false, instancia.getTipo());
                
                if(instancia instanceof VariableArray) {
                    // Necesito copiarlo en una variable (por la sintaxis que tiene CTD)
                    PLXC.out.println(var.getID() + " = " + instancia.getID() + ";");
                    PLXC.out.print(var.getID() + " = " + var.getID());
                } else {
                    PLXC.out.print(var.getID() + " = " + instancia.getID());
                }

                switch(metodo) {
                    case Metodos.SUMA:
                        PLXC.out.print(" +r ");
                        break;
                    case Metodos.RESTA:
                        PLXC.out.print(" -r ");
                        break;
                    case Metodos.PRODUCTO:
                        PLXC.out.print(" *r ");
                        break;
                    case Metodos.DIVISION:
                        PLXC.out.print(" /r ");
                        break;
                }

                PLXC.out.println(obj.getID() + ";");

                return var;
            // Operaciones de asignacion
            case Metodos.MAS_IGUAL:
                obj = instancia.generarCodigoMetodo(linea, Metodos.SUMA, params);
                PLXC.out.println(instancia.getID() + " = " + obj.getID() + ";");
                return instancia;
            case Metodos.MENOS_IGUAL:
                obj = instancia.generarCodigoMetodo(linea, Metodos.RESTA, params);
                PLXC.out.println(instancia.getID() + " = " + obj.getID() + ";");
                return instancia;
            case Metodos.POR_IGUAL:
                obj = instancia.generarCodigoMetodo(linea, Metodos.PRODUCTO, params);
                PLXC.out.println(instancia.getID() + " = " + obj.getID() + ";");
                return instancia;
            case Metodos.DIV_IGUAL:
                obj = instancia.generarCodigoMetodo(linea, Metodos.DIVISION, params);
                PLXC.out.println(instancia.getID() + " = " + obj.getID() + ";");
                return instancia;
            // Relacionales
            case Metodos.IGUAL:
            case Metodos.DISTINTO:
            case Metodos.MENOR:
            case Metodos.MENOR_IGUAL:
            case Metodos.MAYOR:
            case Metodos.MAYOR_IGUAL:
                obj = params[0];

                if(!(obj instanceof Instancia)) {
                    throw new ParseException(obj.getNombre() + " no es una instancia (literal o variable)", linea);
                }

                tipo = ((Instancia) obj).getTipo();
                if(tipo != this && tipo != TipoFloat.instancia) {
                    throw new ParseException("Tipo inválido para comparar con " + getNombre(), linea);
                }

                var = new Variable(nuevoNombre(), instancia.getBloque(), false, TipoBool.instancia);
                PLXC.out.println(var.getID() + " = 1;"); // $t0 = 1

                etq = nuevaEtiqueta();
                switch(metodo) {
                    case Metodos.IGUAL:
                        PLXC.out.println("if (" + instancia.getID() + " == " + obj.getID() + ") goto " + etq + ";"); // if (a == p) goto L;
                        break;
                    case Metodos.DISTINTO:
                        PLXC.out.println("if (" + instancia.getID() + " != " + obj.getID() + ") goto " + etq + ";"); // if (a != p) goto L;
                        break;
                    case Metodos.MENOR:
                        PLXC.out.println("if (" + instancia.getID() + " < " + obj.getID() + ") goto " + etq + ";"); // if (a < p) goto L;
                        break;
                    case Metodos.MENOR_IGUAL:
                        PLXC.out.println("if (" + instancia.getID() + " < " + obj.getID() + ") goto " + etq + ";"); // if (a < p) goto L;
                        PLXC.out.println("if (" + instancia.getID() + " == " + obj.getID() + ") goto " + etq + ";"); // if (a == p) goto L;
                        break;
                    case Metodos.MAYOR:
                        PLXC.out.println("if (" + obj.getID() + " < " + instancia.getID() + ") goto " + etq + ";"); // if (p < a) goto L;
                        break;
                    case Metodos.MAYOR_IGUAL:
                        PLXC.out.println("if (" + obj.getID() + " < " + instancia.getID() + ") goto " + etq + ";"); // if (p < a) goto L;
                        PLXC.out.println("if (" + instancia.getID() + " == " + obj.getID() + ") goto " + etq + ";"); // if (a == p) goto L;
                        break;
                }

                PLXC.out.println(var.getID() + " = 0;"); // $t0 = 0;

                PLXC.out.println(etq + ":"); // L:

                return var;
            case Metodos.SIG:
                PLXC.out.println(instancia.getID() + " = " + instancia.getID() + " +r 1;"); // $t0 = a + 1;

                return instancia;
            case Metodos.ANT:
                PLXC.out.println(instancia.getID() + " = " + instancia.getID() + " -r 1;"); // $t0 = a - 1;

                return instancia;
            case Metodos.OPUESTO:
                var = new Variable(nuevoNombre(), instancia.getBloque(), false, this);
                PLXC.out.println(var.getID() + " = 0 -r " + instancia.getID() + ";"); // $t0 = 0 - a;

                return var;
            default:
                throw new ParseException("Método " + metodo + " no permitido para " + getNombre(), linea);
        }

        return null; // Aquí van todos los métodos que no devuelven nada
    }
}
