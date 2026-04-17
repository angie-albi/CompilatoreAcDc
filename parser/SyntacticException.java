package parser;

/**
 * Eccezione personalizzata per gestire gli errori sintattici del Parser
 */
@SuppressWarnings("serial")
public class SyntacticException extends Exception{
	
	/**
	 * Costruttore con solo il messaggio di errore testuale
	 * 
	 * @param message Il messaggio di errore
	 */
	public SyntacticException(String message) {
		super(message);
	}

	/**
	 * Costruttore per il chainig delle eccezioni
	 * Permette di prendere un'eccezione lanciata dallo Scanner (LexicalException) 
	 * e impacchettarla come eccezione del Parser
	 * 
	 * @param message Il messaggio di errore del parser
	 * @param cause L'eccezione originale scatenante
	 */
	public SyntacticException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
     * Costruisce l'eccezione formattando il messaggio con riga e causa
     * 
     * @param riga  Riga in cui si è verificato l'errore
     * @param causa Descrizione specifica del problema
     */
    public SyntacticException(int riga, String causa) {
        // Chiamiamo il costruttore della superclasse (Exception) aggiungendo il messaggio
        super("Errore Sintattico alla riga " + riga + ": " + causa);
    }

    /**
     * Costruttore per creare un'eccezione wrappata
     * 
     * @param e Eccezione 
     */
	public SyntacticException(Throwable e) {
		super("Eccezione: " + e.getMessage(), e);
	}
}