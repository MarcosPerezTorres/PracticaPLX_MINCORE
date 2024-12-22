import java.util.Vector;
public class Bloque extends Instruccion{
	private Vector<Instruccion> instrucciones;
	
	public Bloque ( int linea ) {
		super ( linea );
		instrucciones = new Vector<>();
	}
	
	public void add ( Instruccion instruccion ) {
		instrucciones.add( instruccion);
	}
	
	@Override
	public Object generarCodigo() throws Exception {
		for( Instruccion instruccion: instrucciones) {
			instruccion.generarCodigo();
		}
		return null;
	}

}
