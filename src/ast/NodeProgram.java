package ast;

import java.util.ArrayList;

import visitor.IVisitor;

/**
 * Rappresenta il nodo radice dell'AST per l'intero programma
 */
public class NodeProgram extends NodeAst {

	private ArrayList<NodeDecSt> decSt;

	/**
	 * Costruisce il nodo principale del programma
	 * 
	 * @param decSt La lista dei nodi che compongono il programma
	 */
	public NodeProgram(ArrayList<NodeDecSt> decSt) {
		super();
		this.decSt = decSt;
	}
	
	/**
	 * Restituisce la lista di tutte le dichiarazioni e istruzioni
	 * 
	 * @return Un'ArrayList contenente i nodi figli di tipo NodeDecSt
	 */
	public ArrayList<NodeDecSt> getDecSt() {
		return decSt;
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
	 * Restituisce una rappresentazione testuale dell'intero programma, 
	 * includendo a cascata tutti i nodi figli contenuti nella lista
	 * 
	 * @return Stringa formattata come <Program,...>
	 */
	@Override
	public String toString() {
		return "<Program," + decSt + ">";
	}
}