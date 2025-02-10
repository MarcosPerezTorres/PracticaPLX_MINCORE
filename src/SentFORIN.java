import java.text.ParseException;

public class SentFORIN extends Instruccion {
    // La lvalue de control (es una Instruccion que al generar código devuelve la variable a asignar)
    private Instruccion control;
    // La expresión que debe evaluarse a un array (se espera, que al generar código, devuelva un variableArray)
    private Instruccion arrayExpr;
    // El cuerpo del bucle que se ejecutará en cada iteración
    private Instruccion cuerpo;

    public SentFORIN(int linea, Instruccion control, Instruccion arrayExpr, Instruccion cuerpo){
        super(linea);
        this.control = control;
        this.arrayExpr = arrayExpr;
        this.cuerpo = cuerpo;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        // Se evalúa la expresión del array; se espera que devuelva una VariableArray
        Objeto arrayObj = arrayExpr.generarCodigo();
        if(!(arrayObj instanceof Instancia)){
            throw new ParseException("La expresión en 'in' debe devolver un instancia (literal o variable)", getLinea());
        }
        if(!(arrayObj instanceof VariableArray)){
            throw new ParseException("La expresión en 'in' no devuelve un array", getLinea());
        }
        VariableArray arr = (VariableArray) arrayObj;
        if(!(arr.getTipo() instanceof TipoArray)){
            throw new ParseException("La expresión en 'in' no es de tipo array", getLinea());
        }
        TipoArray tipoArr = (TipoArray) arr.getTipo();
        int arraySize = tipoArr.getSize();

        // Se genera un temporal para el índice del bucle y se inicializa a 0
        String indexTemp = Objeto.nuevoNombre();
        PLXC.out.println(indexTemp + "= 0;");

        // Se generan las etiquetas de inicio y fin del bucle
        String etqInicio = Objeto.nuevaEtiqueta();
        String etqFin = Objeto.nuevaEtiqueta();
        PLXC.out.println(etqInicio + ":");

        // Condición de salida: si el índice es mayor o igual que el tamaño del array, salimos
        PLXC.out.println("if (" + arraySize + " < " + indexTemp + ") goto " + etqFin + ";");
        PLXC.out.println("if (" + arraySize + " == " + indexTemp + ") goto " + etqFin + ";");

        // Se genera el código de la lvalue de control
        Objeto lvalue = control.generarCodigo();
        if(!(lvalue instanceof Instancia)){
            throw new ParseException("La lvalue de control debe ser una instancia", getLinea());
        }
        // Se asigna el elmento actual del array a lvalue, utilizando el método getID(indexTemp)
        PLXC.out.println(lvalue.getID() + " = " + arr.getID(indexTemp) + ";");

        // Se genera el código del cuerpo del bucle
        cuerpo.generarCodigo();

        // Se incrementa el índice y se regresa al inicio del bucle
        PLXC.out.println(indexTemp + " = " + indexTemp + " + 1;");
        PLXC.out.println("goto " + etqInicio + ";");

        // Se coloca la etiqueta al final del bucle
        PLXC.out.println(etqFin + ":");


        return null;
    }

    
    
}
