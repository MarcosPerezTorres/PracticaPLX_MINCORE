import java.util.Objects;

public abstract class Objeto implements Comparable<Objeto>{
	 private String nombre;
	 private int bloque;
	 private boolean mutable;
	 
	 private static int numObj = 0;
	 
	 public static String newNombObj() {
		 String n = new String("$t"+Integer.toString(numObj));
		 numObj++;
		 return n;
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
	 
	 public abstract Objeto generarCodigoMetodo(String metodo, Objeto[] param) throws Exception;
	    
	    @Override
	    public int compareTo (Objeto o){
		 if(o == null) {
			 throw new NullPointerException("El objeto comparado no puede ser null");
		 }
		 
		 // Comparar primero por bloque
		 int bloqueComparison = Integer.compare(this.bloque, o.bloque);
		 if(bloqueComparison != 0) {
			 return bloqueComparison;
		 }
		 
		 // Si los bloques son iguales, comparar por nombre
		 if (this.nombre == null && o.nombre != null) {
			 
		 }
	        return 0;
	    }

	    @Override
	    public boolean equals(Object o){

	    }
}
