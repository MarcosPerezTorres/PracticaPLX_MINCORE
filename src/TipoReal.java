import java.text.ParseException;

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
                obj = param[0];
                
                if(!(obj instanceof Instancia)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", linea);
                }
                
                tipo = ((Instancia) obj).getTipo();
                if(tipo != this) {
                    obj = obj.generarCodigoMetodo(Metodos.CAST, new Objeto[]{this}, linea);
                }

                PLXC.out.println(instancia.getIDC() + " = " + obj.getIDC() + ";");
                return param[0];
            case Metodos.CREAR_LITERAL:
                PLXC.out.println(instancia.getIDC() + " = " + ((Literal) instancia).getValor() + ";");
                return instancia;
            
            case Metodos.CAST:
                if(!(param[0] instanceof Tipo)) {
                    throw new ParseException(param[0].getNombre() + " no es un tipo", linea);
                }

                switch(param[0].getNombre()) {
                    case Predefinidos.ENTERO:
                        var = new Variable(newNombObj(), instancia.getBloque(), false, (Tipo) param[0]);
                        PLXC.out.println(var.getIDC() + " = (int) " + instancia.getIDC() + ";");
                        return var;
                    case Predefinidos.CARACTER:
                        var = new Variable(newNombObj(), instancia.getBloque(), false, (Tipo) param[0]);
                        PLXC.out.println(var.getIDC() + " = " + instancia.getIDC() + ";");
                        return var;
                    case Predefinidos.REAL:
                        return instancia;
                    default:
                        throw new ParseException("No se puede convertir un " + getNombre() + " a " + param[0].getNombre(), linea);
                }
                 // Operaciones
            case Metodos.SUMA:
            case Metodos.RESTA:
            case Metodos.PRODUCTO:
            case Metodos.DIVISION:
                if(param == null) {
                    throw new ParseException("No se han pasado parámetros para " + metodo, linea);
                }

                obj = param[0];

                if(!(obj instanceof Instancia)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", linea);
                }

                tipo = ((Instancia) obj).getTipo();

                switch(tipo.getNombre()) {
                    case Predefinidos.REAL:
                       break;
                    case Predefinidos.CARACTER:
                        if(!metodo.equals(Metodos.SUMA) && !metodo.equals(Metodos.RESTA)) {
                            throw new ParseException("Método " + metodo + " no aplicable entre " + getNombre() + " y " + Predefinidos.CARACTER, linea);
                        }
                    case Predefinidos.ENTERO:
                        // Si el operando es un entero, me convierto a real y dejo a la clase TipoFloat que haga la operación
                        obj = obj.generarCodigoMetodo(Metodos.CAST, new Objeto[]{this}, linea);
                        break;
                    default:
                        throw new ParseException("Tipo inválido para operar con " + getNombre(), linea);
                }


                var = new Variable(newNombObj(), instancia.getBloque(), false, this);
				PLXC.out.print(var.getIDC() + " = " + instancia.getIDC());

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

                PLXC.out.println(obj.getIDC() + ";");
                return var;
            case Metodos.MODULO:
                obj = param[0];

                if(!(obj instanceof Instancia)) {
                    throw new ParseException(obj.getNombre() + " no es una instancia (literal o variable)", linea);
                }

                if(((Instancia) obj).getTipo() != this) {
                    obj = obj.generarCodigoMetodo(Metodos.CAST, new Objeto[]{this}, linea);
                }

                Objeto cociente = instancia.generarCodigoMetodo(Metodos.DIVISION, param, linea); // $t0 = a / b;
                Objeto mult = cociente.generarCodigoMetodo(Metodos.PRODUCTO, param, linea); // $t1 = $t0 * b;
                return instancia.generarCodigoMetodo(Metodos.RESTA, new Objeto[]{mult}, linea); // $t2 = a - $t1

            default:
                throw new ParseException("Método " + metodo + " no permitido para " + getNombre(), linea);
    }
    return null;
}
}
