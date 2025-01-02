import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    // Bloque <Integer> -> (NombreObj <String> -> Obj <Objeto>)

    private Map<Integer, Map<String, Objeto>> tabla;

    public TablaSimbolos() {
        tabla = new HashMap<>();
    }

    public void insertarObjeto(Objeto obj) {
        Integer bloque = Integer.valueOf(obj.getBloque());
        Map<String, Objeto> subtabla = tabla.get(bloque);
        if(subtabla == null) {
            Map<String, Objeto> st = new HashMap<>();
            st.put(obj.getNombre(), obj);
            tabla.put(bloque, st);
        } else {
            subtabla.put(obj.getNombre(), obj);
        }
    }

    public Objeto buscar(String nombre) {
        int mayor = 0;
        Objeto obj = null;
        for(var entry : tabla.entrySet()) {
            if(entry.getKey() >= mayor && entry.getValue().containsKey(nombre)) {
                mayor = entry.getKey();
                obj = entry.getValue().get(nombre);
            }
        }

        return obj;
    }

    public void eliminarBloque(int bloque) {
        tabla.remove(bloque);
    }

    public Variable declararVariable(int linea, Variable v) throws Exception {
        Objeto obj = buscar(v.getNombre());

        if(obj != null && obj.getBloque() == v.getBloque()) {
            throw new ParseException("Variable <" + v.getNombre() + "> ya declarada en el mismo bloque", linea);
        }
        insertarObjeto(v);

        return v;
    }

    @Override
    public String toString() {
        return tabla.toString();
    }
}