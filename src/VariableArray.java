public class VariableArray extends Variable {
    String desp;

    public VariableArray(String nombre, String desp, int bloque, boolean mutable, Tipo tipo) {
        super(nombre, bloque, mutable, tipo);
        this.desp = desp;
    }

    public String getDesp() { return this.desp; }

    @Override
    public String getID() {
        return getNombre() + "[" + this.desp + "]";
    }

    public String getID(String s) {
        return getNombre() + "[" + s + "]";
    }

    public String getID(int idx) {
        return getNombre() + "[" + idx + "]";
    }
}
