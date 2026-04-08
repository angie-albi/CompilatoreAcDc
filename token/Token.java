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
     *  Costruttore con il valore testuale specifico
     */
	public Token(int riga, TokenType tipo, String valore) {
		this.riga = riga;
		this.tipo = tipo;
		this.valore = valore;
	}
	
	/**
     *  Costruttore senza il valore testuale specifico
     */
	public Token(int riga, TokenType tipo) {
		this(riga, tipo, null);
	}
 
	/**
	 * Ritorna il numero di riga del token
	 */
	public int getRiga() {
		return riga;
	}

	/**
	 * Ritorna il tipo del token
	 */
	public TokenType getTipo() {
		return tipo;
	}

	/**
	 * Ritorna il valore del token
	 */
	public String getValore() {
		return valore;
	}

	/**
	 * Stampa del token
	 */
	@Override
	public String toString() {
		if(valore != null) 
			return "<" + tipo + " , r:" + riga + " , " + valore;
		else
			return "<" + tipo + " , r:" + riga + ">";
	}
}