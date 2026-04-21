package ast;

/**
 * Rappresenta un nodo dell'AST per un identificatore 
 */
public class NodeId extends NodeAst{

	private String name;

	/**
	 * Costruisce un nodo identificatore
	 * 
	 * @param name Il nome testuale della variabile
	 */
	public NodeId(String name) {
		super();
		this.name = name;
	}

	/**
	 * Restituisce il nome della variabile
	 * 
	 * @return La stringa che rappresenta l'identificatore
	 */
	public String getName() {
		return name;
	}

	/**
	 * Restituisce una rappresentazione sintetica del nodo
	 * 
	 * @return Stringa formattata come <Id=...>
	 */
	@Override
	public String toString() {
		return "<Id=" + name + ">";
	}	
}