import java.text.ParseException;

public class TipoInt extends Tipo{
	public static final TipoInt instancia = new TipoInt();
	
	public TipoInt() {
		super(Predefinidos.ENTERO, 0, false);
	}

	@Override
    public Objeto generarCodigoMetodo(String metodo, Objeto[] params, int linea) throws Exception {
        return null;
    }

    @Override
    public Objeto generarCodigoInstancia(Instancia instancia, String metodo, Objeto[] params, int linea) throws Exception {
        Objeto obj;
        Tipo tipo;
        Variable var;
        String etq;
        
        switch(metodo) {
            case Metodos.MOSTRAR:
                PLXC.out.println("print " + instancia.getIDC() + ";");
                break;
            case Metodos.ASIGNAR: // a = ....
                if(!instancia.getMutable()) {
                    throw new ParseException("No se puede reasignar un valor a la constante <" + instancia.getNombre() + ">", linea);
                }
            case Metodos.CREAR_VARIABLE: // int a = ....
                obj = params[0];
                
                if(!(obj instanceof Instancia)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", linea);
                }
                
                tipo = ((Instancia) obj).getTipo();
                if(tipo != this) {
                    throw new ParseException("<" + obj.getNombre() + "> no es de tipo " + getNombre(), linea);
                }

                PLXC.out.println(instancia.getIDC() + " = " + obj.getIDC() + ";");
                return params[0];
            case Metodos.CREAR_LITERAL:
                PLXC.out.println(instancia.getIDC() + " = " + ((Literal) instancia).getValor() + ";");
                return instancia;
            case Metodos.CAST:
                if(!(params[0] instanceof Tipo)) {
                    throw new ParseException(params[0].getNombre() + " no es un tipo", linea);
                }

                switch(params[0].getNombre()) {
                    case Predefinidos.ENTERO:
                        return instancia;
                    case Predefinidos.CARACTER:
                        var = new Variable(newNombObj(), instancia.getBloque(), false, (Tipo) params[0]);
                        PLXC.out.println(var.getIDC() + " = " + instancia.getIDC() + ";");
                        return var;
                    case Predefinidos.REAL:
                        var = new Variable(newNombObj(), instancia.getBloque(), false, (Tipo) params[0]);
                        PLXC.out.println(var.getIDC() + " = (float) " + instancia.getIDC() + ";");
                        return var;
                    case Predefinidos.BOOL:
                        var = new Variable(newNombObj(), instancia.getBloque(), false, (Tipo) params[0]);
                        etq = newEtiqueta();
                        PLXC.out.println(var.getIDC() + " = 1;"); // $t0 = 1
                        PLXC.out.println("if (" + instancia.getNombre() + " != 0) goto " + etq + ";"); // if(a != 0) goto L
                        PLXC.out.println(var.getIDC() + " = 0;"); // $t0 = 0
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

                switch(tipo.getNombre()) {
                    case Predefinidos.REAL:
                        // Si el operando es un real, me convierto a real y dejo a la clase TipoFloat que haga la operación
                        var = (Variable) instancia.generarCodigoMetodo(Metodos.CAST, new Objeto[]{TipoReal.instancia}, linea);
                        return var.generarCodigoMetodo(metodo, params, linea);
                    case Predefinidos.CARACTER:
                        if(!metodo.equals(Metodos.SUMA) && !metodo.equals(Metodos.RESTA)) {
                            throw new ParseException("Método " + metodo + " no aplicable entre " + getNombre() + " y " + Predefinidos.CARACTER, linea);
                        }
                    case Predefinidos.ENTERO:
                        break;
                    default:
                        throw new ParseException("Tipo inválido para operar con " + getNombre(), linea);
                }

                var = new Variable(newNombObj(), instancia.getBloque(), false, this);
				PLXC.out.print(var.getIDC() + " = " + instancia.getIDC());

                switch(metodo) {
                    case Metodos.SUMA:
                        PLXC.out.print(" + ");
                        break;
                    case Metodos.RESTA:
                        PLXC.out.print(" - ");
                        break;
                    case Metodos.PRODUCTO:
                        PLXC.out.print(" * ");
                        break;
                    case Metodos.DIVISION:
                        PLXC.out.print(" / ");
                        break;
                }

                PLXC.out.println(obj.getIDC() + ";");

                return var;
            case Metodos.MODULO:
                obj = params[0];

                if(!(obj instanceof Instancia)) {
                    throw new ParseException(obj.getNombre() + " no es una instancia (literal o variable)", linea);
                }

                if(((Instancia) obj).getTipo() != this) {
                    obj = obj.generarCodigoMetodo(Metodos.CAST, new Objeto[]{this}, linea);
                }

                Objeto cociente = instancia.generarCodigoMetodo(Metodos.DIVISION, params, linea); // $t0 = a / b;
                Objeto mult = cociente.generarCodigoMetodo(Metodos.PRODUCTO, params, linea); // $t1 = $t0 * b;
                return instancia.generarCodigoMetodo(Metodos.RESTA, new Objeto[]{mult}, linea); // $t2 = a - $t1
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
                if(tipo != this && tipo != TipoReal.instancia) {
                    throw new ParseException("Tipo inválido para comparar con " + getNombre(), linea);
                }

                var = new Variable(newNombObj(), instancia.getBloque(), false, TipoBool.instancia);
                PLXC.out.println(var.getIDC() + " = 1;"); // $t0 = 1

                etq = newEtiqueta();
                switch(metodo) {
                    case Metodos.IGUAL:
                        PLXC.out.println("if (" + instancia.getIDC() + " == " + obj.getIDC() + ") goto " + etq + ";"); // if (a == p) goto L;
                        break;
                    case Metodos.DISTINTO:
                        PLXC.out.println("if (" + instancia.getIDC() + " != " + obj.getIDC() + ") goto " + etq + ";"); // if (a != p) goto L;
                        break;
                    case Metodos.MENOR:
                        PLXC.out.println("if (" + instancia.getIDC() + " < " + obj.getIDC() + ") goto " + etq + ";"); // if (a < p) goto L;
                        break;
                    case Metodos.MENOR_IGUAL:
                        PLXC.out.println("if (" + instancia.getIDC() + " < " + obj.getIDC() + ") goto " + etq + ";"); // if (a < p) goto L;
                        PLXC.out.println("if (" + instancia.getIDC() + " == " + obj.getIDC() + ") goto " + etq + ";"); // if (a == p) goto L;
                        break;
                    case Metodos.MAYOR:
                        PLXC.out.println("if (" + obj.getIDC() + " < " + instancia.getIDC() + ") goto " + etq + ";"); // if (p < a) goto L;
                        break;
                    case Metodos.MAYOR_IGUAL:
                        PLXC.out.println("if (" + obj.getIDC() + " < " + instancia.getIDC() + ") goto " + etq + ";"); // if (p < a) goto L;
                        PLXC.out.println("if (" + instancia.getIDC() + " == " + obj.getIDC() + ") goto " + etq + ";"); // if (a == p) goto L;
                        break;
                }

                PLXC.out.println(var.getIDC() + " = 0;"); // $t0 = 0;

                PLXC.out.println(etq + ":"); // L:

                return var;
            case Metodos.SIGUIENTE:
                PLXC.out.println(instancia.getIDC() + " = " + instancia.getIDC() + " + 1;"); // $t0 = a + 1;

                return instancia;
            case Metodos.ANTERIOR:
                PLXC.out.println(instancia.getIDC() + " = " + instancia.getIDC() + " - 1;"); // $t0 = a - 1;

                return instancia;
            case Metodos.OPUESTO:
                var = new Variable(newNombObj(), instancia.getBloque(), false, this);
                PLXC.out.println(var.getIDC() + " = 0 - " + instancia.getIDC() + ";"); // $t0 = 0 - a;

                return var;
            default:
                throw new ParseException("Método " + metodo + " no permitido para " + getNombre(), linea);
        }

        return null; // Aquí van todos los métodos que no devuelven nada
    }
}
