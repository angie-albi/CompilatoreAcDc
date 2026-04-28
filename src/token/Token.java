package token;
/**
 *  Classe che descrive un token, ovvero descrive un insieme di caratteri che hanno lo 
 *  stesso significato (es. identificatori, operatori, keyworks, numeri, delimitatori, ecc.) 
 */
public class Token {
	
	private int riga;		//riga dove è stato trovato il token
    private TokenType tipo;	//tipo di token
    private String valore;	//valore testuale
    
    /**
     * Costruttore con il valore testuale specifico
     * 
     * @param riga La riga in cui è stato trovato il token
     * @param tipo Il tipo del token
     * @param valore Il valore testuale del token
     */
	public Token(int riga, TokenType tipo, String valore) {
		this.riga = riga;
		this.tipo = tipo;
		this.valore = valore;
	}
	
	/**
	 * Costruisce un token senza un valore testuale specifico
	 * Utile per token il cui tipo è già sufficiente a descriverli (es. operatori o delimitatori)
	 * 
	 * @param riga La riga del codice sorgente in cui è stato trovato il token
	 * @param tipo Il tipo logico del token 
	 */
	public Token(int riga, TokenType tipo) {
		this(riga, tipo, null);
	}
 
	/**
	 * Restituisce il numero di riga in cui il token è stato individuato
	 * 
	 * @return La riga del file sorgente
	 */
	public int getRiga() {
		return riga;
	}

	/**
	 * Restituisce la categoria logica a cui appartiene il token
	 * 
	 * @return Il tipo logico di token
	 */
	public TokenType getTipo() {
		return tipo;
	}

	/**
	 * Restituisce il testo esatto associato al token, se presente
	 * 
	 * @return Il valore testuale del token, oppure null se non specificato
	 */
	public String getValore() {
		return valore;
	}

	/**
	 * Genera una rappresentazione testuale del token formattata
	 * 
	 * @return Una stringa nel formato <...,r=...,val=....> oppure <...,r=...>.
	 */
	@Override
	public String toString() {
		if(valore != null) 
			return "<" + tipo + ",r=" + riga + ",val=" + valore + ">";
		else
			return "<" + tipo + ",r=" + riga + ">";
	}
}