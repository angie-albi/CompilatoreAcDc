package symbolTable;

import ast.LangType;

/**
 * Rappresenta gli attributi associati a un identificatore all'interno della Symbol Table
 */
public class Attributes {
	
	private LangType tipo;

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
	 * Restituisce una rappresentazione testuale degli attributi
	 * 
	 * @return Una stringa nel formato <Attributes,...>
	 */
	@Override
	public String toString() {
		return "<Attributes," + tipo + ">";
	}
}