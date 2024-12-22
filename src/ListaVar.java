
public class ListaVar extends Bloque {
	private Tipo tipo;
	
	public ListaVar(int Linea, Tipo tipo) {
		super(linea);
		super.tipo = tipo;
	}
	
	public Tipo getTipo() {
		return tipo;
	}

}
