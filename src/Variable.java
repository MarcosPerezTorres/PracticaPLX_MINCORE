
public class Variable {
 public Variable String nombre, int bloque, boolean mutable, Tipo tipo){
	 super(nombre,bloque,mutable,tipo);
 }
 
 @Override
 public Objeto generarCodigoMetodo(String metodo, Objeto[] param, int linea) {
	 return getTipo().generarCodigoInstancia(this,metodo,param,linea);
	 
 }
	
	
}
