public class Case {
    private Instruccion valor;
    private Bloque cuerpo;
    private Instruccion breakO;

    public Case(Instruccion valor, Bloque cuerpo, Instruccion breakO){
        this.valor = valor;
        this.cuerpo = cuerpo;
        this.breakO = breakO;
    }

    public Instruccion getExp(){
        return valor;
    }

    public Bloque getCuerpo(){
        return cuerpo;
    }

    public boolean tieneBreak(){
        return breakO != null;
    }
}