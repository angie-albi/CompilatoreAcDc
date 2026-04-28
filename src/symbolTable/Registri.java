package symbolTable;

/**
 * Gestore dei registri per la generazione del codice sorgente in 'dc'
 * Assegna progressivamente i caratteri alfabetici da 'a' a 'z'
 */
public class Registri {
	
	private static char prossimoRegistro = 'a';
	
	/**
	 * Resetta il generatore di registri
	 * 
	 * È fondamentale chiamare questo metodo prima di ogni nuova compilazione (visita)
	 */
	public static void init() {
		prossimoRegistro = 'a';
	}

	/**
	 * Fornisce un nuovo registro disponibile
	 * 
	 * @return Il carattere rappresentante il registro (es. 'a', 'b', ...),
	 * ritorna 0 se i registri (le lettere) sono esauriti
	 */
	public static char newRegister() {
		// Controllo se abbiamo superato la lettera 'z'
		if (prossimoRegistro > 'z') {
			return 0; 
		}
		
		char registroAssegnato = prossimoRegistro;
		prossimoRegistro++; 
		
		return registroAssegnato;
	}
}