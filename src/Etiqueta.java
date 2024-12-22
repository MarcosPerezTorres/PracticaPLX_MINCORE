
public class Etiqueta extends Objeto {
	private boolena puesta;
	
	public Etiqueta(String nombre, int bloque) {
		super(nombre,bloque,false);
		puesta = false;
	}
	
	@Override
	public String getIDC() {
		return getNombre();
	}

	public Objeto generarCodigoMetodo(String metodo, Objeto[] params, int) {
		switch(metodo) {
		case.METODOS.PONERETIQ:
			if(puesta) {
				throw new ParseException(" Las etiquetas solo se pueden poner")
			}
			puesta = true;
			PLXC.out.println( getIDC() + ":");
			break;
		case Metodos.SALTARETIQ:
			PLXC.out.println()Sent
		}
	}
}
