import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;

public class PLXC {
	public static PrintStream out;
	public static Yylex lex;

	public static void main(String argv[]) {
		try {
			Reader in = new InputStreamReader(System.in);	
			out = System.out;
			if (argv.length>0) {
				in = new FileReader(argv[0]);
			}
			if (argv.length>1) {
				out = new PrintStream(new FileOutputStream(argv[1]));
			}
			lex = new Yylex(in);
			parser p = new parser(lex);
			Instruccion i = (Instruccion) p.parse().value;
			i.generarCodigo();
		} catch (ParseException e) {
			// Errores en el código a compilar
			PLXC.out.println("# Parse Error");
			PLXC.out.println("error;");
			PLXC.out.println("halt;");

			System.err.println("Error: " + e.getMessage());
			System.err.println("Línea: " + e.getErrorOffset());
			System.exit(-1);
		} catch (Exception e) {
			// Errores internos
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}
}
