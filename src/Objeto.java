public abstract class Objeto implements Comparable<Objeto> {
    private String nombre;
    private int bloque;
    private boolean mutable;

    private static int nombreId = 0;
    private static int etqId = 0;

    public static String nuevoNombre() {
        String nombre = "$t" + nombreId;
        nombreId++;
        return nombre;
    }

    public static String nuevaEtiqueta() {
        String etq = "L" + etqId;
        etqId++;
        return etq;
    }

    public Objeto(String nombre, int bloque, boolean mutable) {
        this.nombre = nombre;
        this.bloque = bloque;
        this.mutable = mutable;
    }

    public String getNombre() { return this.nombre; }

    public int getBloque() { return this.bloque; }

    public boolean esMutable() { return this.mutable; }

    public String getID() { return this.nombre + "$" + this.bloque; }

    public abstract Objeto generarCodigoMetodo(int linea, String metodo, Objeto[] params) throws Exception;

    @Override
    public int compareTo(Objeto obj) {
        if(obj == null) {
            throw new NullPointerException("El objeto con el que se intenta comparar es nulo");
        }

        if(this.bloque == obj.bloque) {
            return this.nombre.compareTo(obj.nombre);
        } else {
            return this.bloque - obj.bloque;
        }
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq = (obj instanceof Objeto);
        if(eq) {
            Objeto o = (Objeto) obj;

            eq = this.nombre.equals(o.nombre) && this.bloque == o.bloque;
        }

        return eq;
    }
}