import java.text.ParseException;

public class Etiqueta extends Objeto {
    private boolean usada;

    public Etiqueta(String nombre, int bloque) {
        super(nombre, bloque, false);
        usada = false;
    }

    @Override
    public Objeto generarCodigoMetodo(int linea, String metodo, Objeto[] params) throws Exception {
        switch(metodo) {
            case Metodos.PONER_ETQ:
                if(usada) {
                    throw new ParseException("Etiqueta <" + getNombre() + "> ya usada", linea);
                }
                PLXC.out.println(getNombre() + ": ");
                break;
            case Metodos.SALTAR_A_ETQ:
                PLXC.out.println("goto " + getNombre() + ";");
                break;
            default:
                throw new UnsupportedOperationException(metodo + " no es un método válido para etiquetas");
        }

        return null;
    }
}
