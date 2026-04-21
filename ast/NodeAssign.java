package ast;

/**
 * Nodo AST per un'istruzione di assegnamento
 */
public class NodeAssign extends NodeStat {
	private NodeId id;
	private NodeExpr expr;
	
	/**
	 * Costruttore per creare un nuovo nodo di assegnamento
	 * 
	 * @param id Variabile a cui assegnare il valore
	 * @param expr Valore da assegnare
	 */
	public NodeAssign(NodeId id, NodeExpr expr) {
		super();
		this.id = id;
		this.expr = expr;
	}
	
	/**
	 * Restituisce l'identificatore della variabile oggetto dell'assegnamento
	 * 
	 * @return Il nodo NodeId associato a questo assegnamento
	 */
	public NodeId getId() {
		return id;
	}
	
	/**
	 * Restituisce l'espressione che viene assegnata alla variabile
	 * 
	 * @return Il nodo NodeExpr associato a questo assegnamento
	 */
	public NodeExpr getExpr() {
		return expr;
	}
	
	/**
	 * Restituisce una rappresentazione testuale del nodo di assegnamento
	 * 
	 * @return Una stringa nel formato <Assign,id=...,expr=...>
	 */
	@Override
	public String toString() {
		return "<Assign,id=" + id + ",expr=" + expr + ">";
	}
}