package ast;

import java.util.ArrayList;

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