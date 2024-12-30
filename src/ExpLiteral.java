public class ExpLiteral extends Instruccion {
	private Literal l;

	public ExpLiteral(int linea, Literal l) {
		super(linea);
		this.l = l;
	}
	
	@Override
	public Objeto generarCodigo() throws Exception {
		return l;
	}
}
