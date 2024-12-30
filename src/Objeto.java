public abstract class Objeto implements Comparable<Objeto>{
	private String nombre;
	private int bloque;
	private boolean mutable;
	 
	private static int numObj = 0;
	private static int numEtq = 0;
	 
	public static String newNombObj() {
		String n = "$t" + numObj;
		numObj++;
		return n;
	}

	public static String newEtiqueta() {
		String etq = "L" + numEtq;
		numEtq++;
		return etq;
	}
	 
	public Objeto (String nombre, int bloque, boolean mutable) {
		this.nombre = nombre;
		this.bloque = bloque;
		this.mutable = mutable;
	}
	 
	public String getNombre() {
		return nombre;
	}
	 
	public int getBloque() {
		return bloque;
	}
	 
	public boolean getMutable() {
		return mutable;
	}
	 
	public String getIDC() {
		return nombre+"$"+Integer.toString(bloque);
	}
	 
	public abstract Objeto generarCodigoMetodo(String metodo, Objeto[] param, int linea) throws Exception;
	    
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
