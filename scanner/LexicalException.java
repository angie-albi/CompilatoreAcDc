package scanner;

/**
 * Classe per le eccezzioni lessicali
 */
@SuppressWarnings("serial")
public class LexicalException extends Exception {

	// Costruttori
	public LexicalException() {
		super();
	}

	public LexicalException(String message) {
		super(message);
	}

}
