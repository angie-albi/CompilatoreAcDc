package ast;

import visitor.IVisitor;

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
	 * Accetta il visitor per l'attraversamento dell'AST (Pattern Visitor)
	 * Il nodo rivela il proprio tipo concreto al visitatore, garantendo che venga
	 * richiamato il metodo visit() corretto per questa specifica classe
	 * 
	 * @param visitor Il visitatore che sta eseguendo l'operazione
	 */
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
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