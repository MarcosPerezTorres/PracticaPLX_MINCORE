import java.text.ParseException;

public class TipoString extends TipoArray {
    public TipoString(int size) {
        super(Predefinidos.STRING, size, TipoChar.instancia);
    }

    public void setSize(int size) { this.size = size; }

    @Override
    public Objeto generarCodigoInstancia(int linea, Instancia instancia, String metodo, Objeto[] params) throws Exception {
        // Parámetro
        Objeto obj;
        Tipo tipoObj;
        VariableArray arrayParam;

        // Instancia
        VariableArray array;
        TipoString tipo = (TipoString) instancia.getTipo();

        Variable v;
        VariableArray vArray;
        String tmp;
        int size;

        switch(metodo) {
            case Metodos.MOSTRAR:
                tmp = nuevoNombre();
                array = (VariableArray) instancia;

                for(int i = 0; i < tipo.getSize(); i++) {
                    PLXC.out.println(tmp + " = " + array.getID(i) + ";");
                    PLXC.out.println("writec " + tmp + ";");
                }

                PLXC.out.println("writec " + "\n".codePointAt(0) + ";");

                break;
            case Metodos.LENGTH:
                v = new Variable(nuevoNombre(), instancia.getBloque(), false, TipoInt.instancia);
                PLXC.out.println(v.getID() + " = " + tipo.getSize() + ";");
                return v;
            case Metodos.CREAR_LITERAL:
                String s = (String) ((Literal) instancia).getValor(); 
                array = new VariableArray(nuevoNombre(), "0", instancia.getBloque(), false, new TipoString(s.length()));

                for(int i = 0; i < s.length(); i++) {
                    PLXC.out.println(array.getID(i) + " = " + Character.codePointAt(s, i) + ";");
                }

                return array;
            case Metodos.CREAR_VARIABLE:
            case Metodos.ASIGNAR:
                obj = params[0];

                if(!(obj instanceof Instancia)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", linea);
                }

                tipoObj = ((Instancia) obj).getTipo();
            
                if(!(tipoObj instanceof TipoString)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es de tipo " + getNombre(), linea);
                }
                
                array = (VariableArray) instancia;
                arrayParam = (VariableArray) params[0];

                size = ((TipoString) tipoObj).getSize();
                tipo.setSize(size);

                tmp = nuevoNombre();

                for(int i = 0; i < size; i++) {
                    PLXC.out.println(tmp + " = " + arrayParam.getID(i) + ";"); // tmp = arrayParam[i]
                    PLXC.out.println(array.getID(i) + " = " + tmp + ";"); // array[i] = tmp
                }

                return arrayParam;
            case Metodos.INDEXAR:
                array = (VariableArray) instancia;
                obj = params[0];

                if(!(obj instanceof Instancia)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", linea);
                }

                tipoObj = ((Instancia) obj).getTipo();
                if(tipoObj != TipoInt.instancia) {
                    throw new ParseException("<" + obj.getNombre() + "> no es de tipo " + TipoInt.instancia.getNombre(), linea);
                }

                size = tipo.getSize();

                String etqError = nuevaEtiqueta();
                String etqFin = nuevaEtiqueta();

                // Se sale fuera del rango (0 <= idx < limite)
                PLXC.out.println("if (" + obj.getID() + " < 0) goto " + etqError + ";");
                PLXC.out.println("if (" + size + " < " + obj.getID() + ") goto " + etqError + ";");
                PLXC.out.println("if (" + size + " == " + obj.getID() + ") goto " + etqError + ";");
                PLXC.out.println("goto " + etqFin + ";");

                PLXC.out.println(etqError + ":");
                PLXC.out.println("error;");
                PLXC.out.println("halt;");

                PLXC.out.println(etqFin + ":");

                return new VariableArray(instancia.getNombre(), obj.getID(), instancia.getBloque(), true, tipo.getTipoBase());
            case Metodos.CAST:
                if(!(params[0] instanceof Tipo)){
                    throw new ParseException(params[0].getNombre() + " no es un tipo", linea);
                }

                Tipo tipoDestino = (Tipo) params[0];

                if (tipoDestino instanceof TipoArray && ((TipoArray) tipoDestino).getTipoBase() instanceof TipoChar){
                    // Convertir String a char[]
                    VariableArray nuevoArray = new VariableArray(Objeto.nuevoNombre(), "0", instancia.getBloque(), false, new TipoArray(this.getSize(),TipoChar.instancia));

                    for (int i = 0; i < this.getSize(); i++){
                        tmp = Objeto.nuevoNombre();
                        PLXC.out.println(tmp + " = " + ((VariableArray) instancia).getID(i) + ";");
                        PLXC.out.println(nuevoArray.getID(i) + " = " + tmp + ";");
                    }
                    return nuevoArray;
                }

                if (tipoDestino instanceof TipoArray && ((TipoArray) tipoDestino).getTipoBase() instanceof TipoInt){
                    // Convertir String a int[]
                    VariableArray nuevoArray = new VariableArray(Objeto.nuevoNombre(), "0", instancia.getBloque(), false, new TipoArray(this.getSize(),TipoInt.instancia));

                    for (int i = 0; i < this.getSize(); i++){
                        tmp = Objeto.nuevoNombre();
                        PLXC.out.println(tmp + " = " + ((VariableArray) instancia).getID(i) + ";");
                        PLXC.out.println(nuevoArray.getID(i) + " = " + tmp + ";");
                    }
                    return nuevoArray;
                }

                throw new ParseException("No se puede convertir string a " + tipoDestino.getNombre(), linea);

            case Metodos.SUMA:
                obj = params[0];

                if(!(obj instanceof Instancia)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", linea);
                }

                tipoObj = ((Instancia) obj).getTipo();
                
                if(!(tipoObj instanceof TipoString)) {
                    throw new ParseException("<" + obj.getNombre() + "> no es de tipo " + getNombre(), linea);
                }

                array = (VariableArray) instancia;
                arrayParam = (VariableArray) params[0];

                size = ((TipoString) arrayParam.getTipo()).getSize();

                tmp = nuevoNombre();
                vArray = new VariableArray(nuevoNombre(), "0", instancia.getBloque(), false, new TipoString(tipo.getSize() + size));

                for(int i = 0; i < tipo.getSize(); i++) {
                    PLXC.out.println(tmp + " = " + array.getID(i) + ";");
                    PLXC.out.println(vArray.getID(i) + " = " + tmp + ";");
                }

                for(int i = 0; i < size; i++) {
                    PLXC.out.println(tmp + " = " + arrayParam.getID(i) + ";");
                    PLXC.out.println(vArray.getID(i + tipo.getSize()) + " = " + tmp + ";");
                }

                return vArray;
            default:
                throw new ParseException("Método " + metodo + " no permitido para tipo " + getNombre(), linea);
        }

        return null;
    }
}
