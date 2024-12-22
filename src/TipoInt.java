
public class TipoInt extends Tipo{
	public static final TipoInt instancia = new TipoInt();
	
	public TipoInt()

	
	@Override
	public Objeto generarCodigoMetodo (String metodo, Objeto[] param, int linea) throws Exception{
		Instancia par;
		Variable v;
		String et1;
		
		switch(metodo) {
		case Metodos.IMPRIMIR:
			PLXC.out.println(" print "+instancia.getIDC()+";");
			break;
		case Metodos.ASIGNA:
			if(!instancia.getMutable()) {
				throw new Parse Exception("("+instancia.getNombre()+") no es una variable")
			}
		case Metodos.CONSTRUCTORCOPIA:
			Objeto p = param[0];
			if(!(p instanceof Instancia)) {
				throw new ParseException("("+ p.getNombre()+") dato requerido a la derecha");
			}
			par = (Instancia) p;
			
			if(par.getTipo() != this) {
				par = (Instancia) par.generarCodigoMetodo( Metodos.CAST, new Objeto[] {this}, linea);
				
				PLXC.out.println(instancia.getIDC() + " = " + par.getIDC() + ";");
				return param[0];
			}
		}
		return null;
	}
}
