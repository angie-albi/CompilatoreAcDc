package ast;

/**
 * Rappresenta un nodo dell'AST per la dichiarazione di una variabile
 */
public class NodeDecl extends NodeDecSt {
	private NodeId id;
	private LangType tipo;
	private NodeExpr init;
	
	/**
	 * Costruisce un nodo di dichiarazione variabile
	 * 
	 * @param id   L'identificatore della variabile
	 * @param tipo Il tipo di dato della variabile 
	 * @param init L'espressione di inizializzazione (null se assente)
	 */
	public NodeDecl(NodeId id, LangType tipo, NodeExpr init) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.init = init;
	}
	
	/**
	 * Restituisce l'identificatore della variabile dichiarata
	 * 
	 * @return Il nodo identificatore associato alla dichiarazione
	 */
	public NodeId getId() {
		return id;
	}
	
	/**
	 * Restituisce il tipo di dato associato alla variabile
	 * 
	 * @return Il tipo di dato della variabile dichiarata
	 */
	public LangType getTipo() {
		return tipo;
	}
	
	/**
	 * Restituisce l'espressione usata per inizializzare la variabile, se presente
	 * 
	 * @return L'espressione di inizializzazione, o null se la variabile non è inizializzata
	 */
	public NodeExpr getInit() {
		return init;
	}
	
	/**
	 * Restituisce una rappresentazione testuale del nodo 
	 * 
	 * @return Una stringa formattata come <Decl,id=...,tipo=...,init=...>
	 */
	@Override
	public String toString() {
		return "<Decl,id=" + id + ",tipo=" + tipo + ",init=" + init + ">";
	}
}