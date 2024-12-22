
public abstract class Instruccion {
	private int linea;
	
	Instruccion ( int linea ){
		this.linea = linea;
	}
	
	public int getLinea () {
		return this.linea;
	}
	
	public abstract void generarCodigo() throws Exception;

}
