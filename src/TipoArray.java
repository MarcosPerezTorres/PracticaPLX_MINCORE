import java.text.ParseException;

public class TipoArray extends Tipo {
    protected int size;
    protected Tipo tipoBase;

    public TipoArray(String nombre, int size, Tipo tipoBase) {
        super(nombre, 0, false);
        this.tipoBase = tipoBase;
        this.size = size;
    }

    public TipoArray(int size, Tipo tipoBase) {
        this("array[" + size + "](" + tipoBase.getNombre() + ")", size, tipoBase);
    }

    public Tipo getTipoBase() { return this.tipoBase; }

    public int getSize() { return this.size; }

    @Override
    public boolean esArrayCompatible(Tipo tipo) {
        if(tipo instanceof TipoArray) {
            TipoArray t = (TipoArray) tipo;
            return size <= t.getSize() && tipoBase.esArrayCompatible(t.getTipoBase());
        } else {
            return false;
        }
    }

    @Override
    public Objeto generarCodigoMetodo(int linea, String metodo, Objeto[] params) throws Exception {
        return null;
    }

    @Override
    public Objeto generarCodigoInstancia(int linea, Instancia instancia, String metodo, Objeto[] params) throws Exception {
        // Parámetro
        Objeto obj;
        Tipo tipoObj;
        VariableArray arrayParam;

        // Instancia
        VariableArray array = (VariableArray) instancia;
        TipoArray tipo = (TipoArray) instancia.getTipo();

        Variable v;
        String tmp, desp, limite;
        int baseSize = 1;

        switch(metodo) {
            case Metodos.MOSTRAR:
                v = new Variable(nuevoNombre(), instancia.getBloque(), true, tipo.getTipoBase());
                desp = nuevoNombre();

                if(tipo.getTipoBase() instanceof TipoArray) {
                    baseSize = ((TipoArray) tipo.getTipoBase()).getSize();
                }

                for(int i = 0; i < tipo.getSize() * baseSize; i++) {
                    // Accedemos al elemento i-ésimo
                    PLXC.out.println(desp + " = " + array.getDesp() + " + " + i + ";"); // desp = desp + i
                    PLXC.out.println(v.getID() + " = " + array.getID(desp) + ";"); // v = array[desp]

                    // Lo imprimimos
                    if(tipo.getTipoBase() == TipoChar.instancia) {
                        PLXC.out.println("printc " + v.getID() + ";");
                    } else {
                        PLXC.out.println("print " + v.getID() + ";");
                    }
                }

                break;
            case Metodos.LENGTH:
                v = new Variable(nuevoNombre(), instancia.getBloque(), false, TipoInt.instancia);
                PLXC.out.println(v.getID() + " = " + tipo.getSize() + ";");
                return v;
            case Metodos.CREAR_VARIABLE:
            case Metodos.ASIGNAR:
                arrayParam = (VariableArray) params[0];
                tipoObj = arrayParam.getTipo();

                if(!tipoObj.esArrayCompatible(tipo)) {
                    throw new ParseException("<" + arrayParam.getNombre() + "> no es compatible con " + tipo.getNombre(), linea);
                }

                tmp = nuevoNombre();
                desp = nuevoNombre();

                if(tipo.getTipoBase() instanceof TipoArray) {
                    baseSize = ((TipoArray) tipo.getTipoBase()).getSize();
                }

                for(int i = 0; i < ((TipoArray) tipoObj).getSize() * baseSize; i++) {
                    PLXC.out.println(desp + " = " + array.getDesp() + " + " + i + ";"); // desp = desp + i
                    PLXC.out.println(tmp + " = " + arrayParam.getID(i) + ";"); // tmp = arrayParam[i]
                    PLXC.out.println(array.getID(desp) + " = " + tmp + ";"); // array[desp + i] = tmp
                }

                return arrayParam;
            case Metodos.INDEXAR:
                obj = params[0];

                if(!(obj instanceof Instancia)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", linea);
                }

                tipoObj = ((Instancia) obj).getTipo();
                if(tipoObj != TipoInt.instancia) {
                    throw new ParseException("<" + obj.getNombre() + "> no es de tipo " + TipoInt.instancia.getNombre(), linea);
                }

                if(tipo.getTipoBase() instanceof TipoArray) {
                    baseSize = ((TipoArray) tipo.getTipoBase()).getSize();
                }

                // Calculamos el nuevo índice (idx = size * i + desp)
                tmp = nuevoNombre(); // idx
                PLXC.out.println(tmp + " = " + baseSize + " * " + obj.getID() + ";");
                PLXC.out.println(tmp + " = " + array.getDesp() + " + " + tmp + ";");

                // Calculamos el límite
                limite = nuevoNombre();
                PLXC.out.println(limite + " = " + tipo.getSize() * baseSize + ";");
                PLXC.out.println(limite + " = " + array.getDesp() + " + " + limite + ";");

                String etqError = nuevaEtiqueta();
                String etqFin = nuevaEtiqueta();

                // Se sale fuera del rango (0 <= idx < limite)
                PLXC.out.println("if (" + tmp + " < 0) goto " + etqError + ";");
                PLXC.out.println("if (" + limite + " < " + tmp + ") goto " + etqError + ";");
                PLXC.out.println("if (" + limite + " == " + tmp + ") goto " + etqError + ";");
                PLXC.out.println("goto " + etqFin + ";");

                PLXC.out.println(etqError + ":");
                PLXC.out.println("error;");
                PLXC.out.println("halt;");

                PLXC.out.println(etqFin + ":");

                return new VariableArray(instancia.getNombre(), tmp, instancia.getBloque(), true, tipo.getTipoBase());
            case Metodos.CAST:
                
                if (!(params[0] instanceof Tipo)){
                    throw new ParseException(params[0].getNombre() + " no es un tipo", linea);
                }

                Tipo tipoDestino = (Tipo) params[0];

                // Convertir int[] a char[]
                if (tipoDestino instanceof TipoArray && !(tipoDestino instanceof TipoString) && ((TipoArray) tipoDestino).getTipoBase() instanceof TipoChar) {
                if (!(this.getTipoBase() instanceof TipoInt)) {
                    throw new ParseException("Solo se puede convertir un array de enteros a array de caracteres", linea);
                }

                TipoArray nuevoTipo = new TipoArray(this.getSize(), TipoChar.instancia);
                VariableArray nuevoArray = new VariableArray(Objeto.nuevoNombre(), "0", instancia.getBloque(), false, nuevoTipo);

                for (int i = 0; i < this.getSize(); i++) {
                    tmp = Objeto.nuevoNombre();
                    PLXC.out.println(tmp + " = " + ((VariableArray) instancia).getID(i) + ";");
                    PLXC.out.println(nuevoArray.getID(i) + " = " + tmp + ";");
                }

                return nuevoArray;
            }

                if (tipoDestino instanceof TipoString){
                    // Convertir char[] a String
                    if (this.getTipoBase() instanceof TipoChar){
                        TipoString nuevoTipo = new TipoString(this.getSize());
                        VariableArray nuevoString = new VariableArray(Objeto.nuevoNombre(), "0", instancia.getBloque(), false, nuevoTipo);

                        for (int i = 0; i < this.getSize(); i++){
                        tmp = Objeto.nuevoNombre();
                        PLXC.out.println(tmp + " = " + ((VariableArray) instancia).getID(i) + ";");
                        PLXC.out.println(nuevoString.getID(i) + " = " + tmp + ";");
                    }
                return nuevoString;
            
                // Convertir int[] a String
                } else if (this.getTipoBase() instanceof TipoInt){
                    TipoString nuevoTipo = new TipoString(this.getSize());
                    VariableArray nuevoString = new VariableArray(Objeto.nuevoNombre(), "0", instancia.getBloque(), false, nuevoTipo);

                    for (int i = 0; i < this.getSize(); i++){
                        tmp = Objeto.nuevoNombre();
                        PLXC.out.println(tmp + " = " + ((VariableArray) instancia).getID(i) + ";");
                        PLXC.out.println(nuevoString.getID(i) + " = " + tmp + ";");
                     }
                return nuevoString;
                }

            }
                throw new ParseException("No se puede convertir " + this.getNombre() + " a " + tipoDestino.getNombre(), linea);
            default:
                throw new ParseException("Método " + metodo + " no permitido para tipo array", linea);
        }

        return null;
    }
}
