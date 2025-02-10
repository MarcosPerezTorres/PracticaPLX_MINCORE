import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentSwitch extends Instruccion {
    private Instruccion exp;        // Instruccion que se evalúa en el switch
    private List<Case> cases;      // Lista de casos
    private Bloque defaultCase;     // Bloque para el caso default (puede estar vacío)

    public SentSwitch(int linea, Instruccion exp, List<Case> cases, Bloque defaultCase){
        super(linea);
        this.exp = exp;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public Objeto generarCodigo() throws Exception {
        Objeto varObj = exp.generarCodigo();    // Evaluamos la expresión del switch
        String etiquetaFinal = Objeto.nuevaEtiqueta();
        String etiquetaDef = Objeto.nuevaEtiqueta();

        // Generar etiquetas para cada caso
        Map<Case, String> etiquetas = new HashMap<>();
        for (Case c : cases){
            etiquetas.put(c, Objeto.nuevaEtiqueta());
        }

        // Comprobar cada caso
        for (Case c : cases){
            Objeto caseExpObj = c.getExp().generarCodigo();
            String etqCase = etiquetas.get(c);

            PLXC.out.println("if (" + varObj.getID() + " == " + caseExpObj.getID() + ") goto " + etqCase + ";");
        }
        if (defaultCase != null){
            PLXC.out.println("goto " + etiquetaDef + ";");
        }

        // Generamos código para cada caso
        for (Case c : cases){
            String etqCase = etiquetas.get(c);
            PLXC.out.println(etqCase + ":");
            c.getCuerpo().generarCodigo();

            if(c.tieneBreak()){
                PLXC.out.println("goto " + etiquetaFinal + ";");
            }
        }

        // Si hay un bloque default, generamos su codigo
        if (defaultCase != null){
            PLXC.out.println(etiquetaDef + ":");
            defaultCase.generarCodigo();
        }

        PLXC.out.println(etiquetaFinal + ":");  // Punto final del switch

        return null;
    }
    
}
