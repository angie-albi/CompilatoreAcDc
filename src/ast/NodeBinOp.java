package ast;

import visitor.IVisitor;

/**
 * Rappresenta un nodo dell'AST per un'operazione matematica binaria
 */
public class NodeBinOp extends NodeExpr {
	
	private LangOper op;
	private NodeExpr sx;
	private NodeExpr dx;
	
	/**
	 * Costruisce un nodo per un'operazione binaria
	 * 
	 * @param op L'operatore matematico applicato agli operandi
	 * @param sx Il nodo espressione che rappresenta l'operando a sinistra
	 * @param dx Il nodo espressione che rappresenta l'operando a destra
	 */
	public NodeBinOp(LangOper op, NodeExpr sx, NodeExpr dx) {
		super();
		this.op = op;
		this.sx = sx;
		this.dx = dx;
	}
	
	/**
	 * Restituisce l'operatore matematico di questa espressione
	 * 
	 * @return L'operatore associato al nodo
	 */
	public LangOper getOp() {
		return op;
	}
	
	/**
	 * Cambia il tipo di operatore dell'esspressione 
	 * Usato per cambiare da DIVIDE a DIV_FLOAT
	 * 
	 * @param op Il nuovo operatore dell'espressione
	 */
	public void setOp(LangOper op) {
	    this.op = op;
	}
	
	/**
	 * Restituisce l'operando a sinistra dell'operatore
	 * 
	 * @return Il nodo espressione sinistro
	 */
	public NodeExpr getSx() {
		return sx;
	}
	
	/**
	 * Restituisce l'operando a destra dell'operatore
	 * 
	 * @return Il nodo espressione destro
	 */
	public NodeExpr getDx() {
		return dx;
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
	 * @return Stringa formattata come <BinOp,...:sx=...,dx=...>
	 */
	@Override
	public String toString() {
		return "<BinOp," + op + ":sx=" + sx + ",dx=" + dx + ">";
	}
}