public class LlamadaMetodo extends Instruccion {
	private Instruccion e1, param[];
	private String metodo;
	
	
	public LlamadaMetodo( int linea, Instruccion e1, String metodo, Instruccion[] param) {
		super(linea);
		this.e1= e1;
		this.metodo = metodo;
		this.param = param;
	}
	
	@Override
	public Objeto generarCodigo() throws Exception{
		Objeto oParam[] = null;
		
		Objeto o1 = e1.generarCodigo();
		
		if(param != null) {
			oParam = new Objeto[param.length];
			
			for( int k = 0; k < param.length; k++) {
				oParam[k] = param[k].generarCodigo();
			}
		}
		
		return o1.generarCodigoMetodo(metodo, oParam, getLinea());
	}
}
