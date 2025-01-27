import java.text.ParseException;

public class TipoChar extends Tipo {
    public static TipoChar instancia = new TipoChar();

    private TipoChar() {
        super(Predefinidos.CHAR, 0, false);
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

        switch(metodo) {
            case Metodos.MOSTRAR:
                if(instancia instanceof VariableArray) {
                    var = new Variable(nuevoNombre(), instancia.getBloque(), false, this);
                    PLXC.out.println(var.getID() + " = " + instancia.getID() + ";");
                    PLXC.out.println("printc " + var.getID() + ";");
                } else {
                    PLXC.out.println("printc " + instancia.getID() + ";");
                }

                break;
            case Metodos.ASIGNAR: // a = ....
                if(!instancia.esMutable()) {
                    throw new ParseException("No se puede reasignar un valor a la constante <" + instancia.getNombre() + ">", linea);
                }
            case Metodos.CREAR_VARIABLE: // char a = ....
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
            case Metodos.CAST:
                if(!(params[0] instanceof Tipo)) {
                    throw new ParseException("<" + params[0].getNombre() + "> no es un tipo", linea);
                }

                switch(params[0].getNombre()) {
                    case Predefinidos.CHAR:
                        return instancia;
                    case Predefinidos.INT:
                        var = new Variable(nuevoNombre(), instancia.getBloque(), false, TipoInt.instancia);
                        PLXC.out.println(var.getID() + " = " + instancia.getID() + ";");
                        return var;
                    default:
                    throw new ParseException("No se puede convertir un " + getNombre() + " a " + params[0].getNombre(), linea);
                }
            case Metodos.SUMA:
            case Metodos.RESTA:
                obj = params[0];

                if(!(obj instanceof Instancia)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", linea);
                }

                tipo = ((Instancia) obj).getTipo();
                if(tipo != TipoInt.instancia && metodo.equals(Metodos.SUMA)) {
                    throw new ParseException("Método " + metodo + " no aplicable entre " + getNombre() + " y " + tipo.getNombre(), linea);
                }

                if(obj instanceof VariableArray) {
                    // Necesito copiarlo en una variable (por la sintaxis que tiene CTD)
                    var = new Variable(nuevoNombre(), instancia.getBloque(), false, tipo);
                    PLXC.out.println(var.getID() + " = " + obj.getID() + ";");
                    obj = var;
                }

                var = new Variable(nuevoNombre(), instancia.getBloque(), false, TipoInt.instancia);
                
                if(instancia instanceof VariableArray) {
                    // Necesito copiarlo en una variable (por la sintaxis que tiene CTD)
                    PLXC.out.println(var.getID() + " = " + instancia.getID() + ";");
                    PLXC.out.print(var.getID() + " = " + var.getID());
                } else {
                    PLXC.out.print(var.getID() + " = " + instancia.getID());
                }

                switch(metodo) {
                    case Metodos.SUMA:
                        PLXC.out.print(" + ");
                        break;
                    case Metodos.RESTA:
                        PLXC.out.print(" - ");
                        break;
                }

                PLXC.out.println(obj.getID() + ";");
                return var;
            default:
                throw new ParseException("Método " + metodo + " no permitido para " + getNombre(), linea);
        }

        return null;
    }
}