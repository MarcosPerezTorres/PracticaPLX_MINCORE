
public class Cortocircuito {

	
	public Cortocircuito (int linea, ) {
		super (linea);
		this.e1 = e1;
		this.metodo = metodo;
		this.e2 = e2;
	}
	
	@Override
	public Objeto generarCodigo() {
		Objeto o1 = e1.generarCodigo();
		
		Etiqueta destino = new Etiqueta( Objeto.newEtiq(), o1.getBloque())
		
		Objeto r = o1.generarCodigoMetodo( metodo, new Objeto[] { destino })
		
		Objeto o2 = e2.generarCodigo();
		
		r.generarCodigoMetodo( Metodos.CONSTRUCTORCOPIA, new Objeto[] {o2}, )
		
		destino.generarCodigoMetodo(Metodos.PONERETIQ, null, getLinea());
		
		return r;
	}
}
