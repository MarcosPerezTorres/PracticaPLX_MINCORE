public abstract class Instancia extends Objeto{
	private final Tipo tipo;
	
	public Instancia (String nombre, int bloque, boolean mutable, Tipo tipo) {
		super(nombre, bloque, mutable);
		this.tipo = tipo;
	}

	public Tipo getTipo() { return this.tipo; }
}
