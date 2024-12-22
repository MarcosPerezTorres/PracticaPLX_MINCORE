
public class CopiaYLlamaMetodo extends Instruccion {
	private Instruccion e1, param[];
	private String metodo;
	
	@Override
	public Objeto generarCodigo() throws Exception {
		Objeto oParam[] = null;
		Objeto r;
		
		Variable o1 = (Variable) e1.generarCodigo();
		
		r = new Variable( Objeto.newNombObj(), o1.getBloque(), true, o1.get)
		r.generarCodigoMetodo(metodos.CONSTRUCTORCOPIA, new Objeto[] {o1}
		
		if(param != null) {
			oParam = new Objeto[param.length];
			
			for ( int k = 0; k < param.length; k++) {
				
			}
		}
	}

}
......