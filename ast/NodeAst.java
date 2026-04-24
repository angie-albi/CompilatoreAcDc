package ast;

import visitor.IVisitor;

/**
 * Rappresenta un nodo dell'AST
 */
public abstract class NodeAst {
	/**
	 * Metodo astratto per l'IVisitor
	 * Ogni nodo concreto dovrà implementare questo metodo per richiamare 
	 * il metodo visit() corrispondente al proprio tipo
	 * 
	 * @param visitor Il visitatore che sta attraversando l'albero
	 */
	public abstract void accept(IVisitor visitor);
	
}