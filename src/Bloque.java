import java.util.Vector;

public class Bloque extends Instruccion {
    protected Vector<Instruccion> instrucciones;

    public Bloque(int linea) {
        super(linea);
        instrucciones = new Vector<>();
    }

    public void addInstruccion(Instruccion instruccion) {
        instrucciones.add(instruccion);
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        for(Instruccion i : instrucciones) {
            i.generarCodigo();
        }

        return null; // No devuelve nada
    }    
}
