package symbolTable;

import ast.LangType;

/**
 * Rappresenta gli attributi associati a un identificatore all'interno della Symbol Table
 */
public class Attributes {
	
	private LangType tipo;
	private char registro;

	/**
	 * Costruisce un nuovo oggetto attributi per una variabile
	 * 
	 * @param tipo Il tipo di dato (LangType) da associare alla variabile
	 */
	public Attributes(LangType tipo) {
		super();
		this.tipo = tipo;
	}

	/**
	 * Restituisce il tipo di dato memorizzato in questi attributi
	 * 
	 * @return Il tipo di dato (INT o FLOAT)
	 */
	public LangType getTipo() {
		return tipo;
	}
	
	/**
	 * Imposta il carattere del registro associato alla variabile
	 * 
	 * @param registro Il carattere (es. 'a', 'b') da assegnare
	 */
	public void setRegistro(char registro) {
		this.registro = registro;
	}

	/**
	 * Restituisce il registro associato alla variabile
	 * 
	 * @return Il carattere del registro
	 */
	public char getRegistro() {
		return registro;
	}
	
	/**
	 * Restituisce una rappresentazione testuale degli attributi
	 * 
	 * @return Una stringa formattata {@code <Attributes,tipo=...,reg=...>}
	 */
	@Override
	public String toString() {
		return "<Attributes,tipo=" + tipo + ", reg=" + registro + ">";
	}
}