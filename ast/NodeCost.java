package ast;

/**
 * Rappresenta un nodo dell'AST per una costante numerica
 */
public class NodeCost extends NodeExpr {

	private String valore;
	private LangType tipo;
	
	/**
	 * Costruisce un nodo per un valore costante
	 * 
	 * @param valore Il valore numerico sotto forma di stringa 
	 * @param tipo  Il tipo di dato del valore 
	 */
	public NodeCost(String valore, LangType tipo) {
		super();
		this.valore = valore;
		this.tipo = tipo;
	}
	
	/**
	 * Restituisce il valore testuale della costante
	 *
	 * @return La stringa che rappresenta il numero
	 */
	public String getValore() {
		return valore;
	}
	
	/**
	 * Restituisce il tipo di dato della costante
	 * 
	 * @return Il tipo associato al valore
	 */
	public LangType getTipo() {
		return tipo;
	}

	/**
	 * Restituisce una rappresentazione sintetica del nodo
	 * 
	 * @return Stringa formattata come <Cost,valore=...,tipo=...>
	 */
	@Override
	public String toString() {
		return "<Cost,valore=" + valore + ",tipo=" + tipo + ">";
	}
}