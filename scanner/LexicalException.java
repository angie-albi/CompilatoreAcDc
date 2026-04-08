package scanner;

/**
 * Eccezione personalizzata per gestire gli errori durante l'analisi lessicale
 */
@SuppressWarnings("serial")
public class LexicalException extends Exception {
    
    /**
     * Costruisce l'eccezione formattando il messaggio con riga e causa
     * 
     * @param riga  Riga in cui si è verificato l'errore
     * @param causa Descrizione specifica del problema
     */
    public LexicalException(int riga, String causa) {
        // Chiamiamo il costruttore della superclasse (Exception) aggiungendo il messaggio
        super("Errore Lessicale alla riga " + riga + ": " + causa);
    }
    
    /**
     * Costruttore per creare un'eccezione wrappata
     * 
     * @param e Eccezione 
     */
	public LexicalException(Throwable e) {
		super("Eccezione: " + e.getMessage(), e);
	}
}