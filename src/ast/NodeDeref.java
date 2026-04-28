package ast;

import visitor.IVisitor;

/**
 * Rappresenta un nodo dell'AST per la dereferenziazione di una variabile
 */
public class NodeDeref extends NodeExpr {

	private NodeId id;

	/**
	 * Costruisce un nodo di dereferenziazione
	 * 
	 * @param id Il nodo identificatore della variabile di cui si vuole leggere il valore
	 */
	public NodeDeref(NodeId id) {
		super();
		this.id = id;
	}

	/**
	 * Restituisce l'identificatore della variabile da leggere
	 * 
	 * @return Il nodo identificatore associato
	 */
	public NodeId getId() {
		return id;
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
	 * @return Stringa formattata come {@code <Deref,id=...>}
	 */
	@Override
	public String toString() {
		return "<Deref,id=" + id + ">";
	}
}