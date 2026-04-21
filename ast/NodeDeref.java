package ast;

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
	 * Restituisce una rappresentazione sintetica del nodo 
	 * 
	 * @return Stringa formattata come <Deref,id=...>
	 */
	@Override
	public String toString() {
		return "<Deref,id=" + id + ">";
	}
}