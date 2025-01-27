import java.text.ParseException;
import java.util.Vector;

public class ExpArray extends Bloque {
    private Vector<Objeto> elementos;

    public ExpArray(int linea) {
        super(linea);
        elementos = new Vector<>();
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        // Generamos el código para cada instrucción  y metemos el resultado en "elementos"
        Objeto obj = instrucciones.get(0).generarCodigo();

        if(!(obj instanceof Instancia)) {
            throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", getLinea());
        }

        int bloque = obj.getBloque();
        Tipo tipoBase = ((Instancia) obj).getTipo();
        
        elementos.add(obj);

        TipoArray tipo = new TipoArray(instrucciones.size(), tipoBase);
        
        for(int i = 1; i < instrucciones.size(); i++) {
            obj = instrucciones.get(i).generarCodigo();
            
            if(!(obj instanceof Instancia)) {
                throw new ParseException("<" + obj.getNombre() + "> no es una instancia (literal o variable)", getLinea());
            } 

            Tipo t = ((Instancia) obj).getTipo();

            // Todos tienen que ser del mismo tipo
            if(!t.esArrayCompatible(tipoBase)) {
                throw new ParseException("<" + obj.getNombre() + "> no es de tipo " + tipoBase.getNombre(), getLinea());
            }

            elementos.add(obj);
        }

        // Esta es la variable que vamos a devolver y que contiene todos los elementos
        VariableArray v = new VariableArray(Objeto.nuevoNombre(), "0", bloque, false, tipo);
        int desp = 0;

        // Recorremos todos los elementos y los asignamos a las posiciones correspondientes del array
        for(int i = 0; i < elementos.size(); i++) {
            obj = elementos.get(i);
            if(obj instanceof VariableArray) {
                VariableArray array = (VariableArray) obj;
                TipoArray tipoArray = (TipoArray) array.getTipo();
                int baseSize = 1;

                if(tipoArray.getTipoBase() instanceof TipoArray) {
                    baseSize = ((TipoArray) tipoArray.getTipoBase()).getSize();
                }

                String tmp = Objeto.nuevoNombre();

                for(int pos = 0; pos < tipoArray.getSize() * baseSize; pos++) {
                    PLXC.out.println(tmp + " = " + array.getID(pos) + ";"); // tmp = t0[0];
                    PLXC.out.println(v.getID(desp) + " = " + tmp + ";"); // t1[desp] = tmp;
                    desp++;
                }
            } else {
                PLXC.out.println(v.getID(desp) + " = " + elementos.get(i).getID() + ";"); // t1[i] = elem;
                desp++;
            }
        }

        return v;
    }
}
