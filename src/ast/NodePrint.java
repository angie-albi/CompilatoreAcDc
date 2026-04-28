package ast;

import visitor.IVisitor;

/**
 * Rappresenta un nodo dell'AST per l'istruzione di stampa
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
	 * @return Stringa formattata come {@code <Print,id=...>}
	 */
	@Override
	public String toString() {
		return "<Print,id=" + id + ">";
	}
}