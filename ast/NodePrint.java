package ast;

/**
 * Rappresenta un nodo dell'AST per l'istruzione di stampa+
 */
public class NodePrint extends NodeStat {

	private NodeId id;

	/**
	 * Costruisce un nodo per l'istruzione print
	 * 
	 * @param id Il nodo identificatore che rappresenta la variabile da stampare
	 */
	public NodePrint(NodeId id) {
		super();
		this.id = id;
	}

	/**
	 * Restituisce l'identificatore della variabile associata all'istruzione di stampa
	 * 
	 * @return Il nodo identificatore della variabile da stampare
	 */
	public NodeId getId() {
		return id;
	}

	/**
	 * Restituisce una rappresentazione sintetica del nodo 
	 * 
	 * @return Stringa formattata come <Print,id=...>
	 */
	@Override
	public String toString() {
		return "<Print,id=" + id + ">";
	}
}