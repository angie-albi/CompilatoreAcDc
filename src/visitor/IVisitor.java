package visitor;

import ast.NodeAssign;
import ast.NodeBinOp;
import ast.NodeCost;
import ast.NodeDecl;
import ast.NodeDeref;
import ast.NodeId;
import ast.NodePrint;
import ast.NodeProgram;

/**
 * Interfaccia del Pattern Visitor per l'Abstract Syntax Tree (AST)
 * Definisce un metodo di visita per ogni nodo concreto dell'albero
 */
public interface IVisitor {
	/**
	 * Visita il nodo radice (principale) dell'intero programma
	 * 
	 * @param node Il nodo NodeProgram da visitare
	 */
	public abstract void visit(NodeProgram node);
	
	/**
	 * Visita un nodo identificatore (il nome di una variabile)
	 * 
	 * @param node Il nodo NodeId da visitare
	 */
	public abstract void visit(NodeId node);
	
	/**
	 * Visita un nodo di dichiarazione di una nuova variabile
	 * 
	 * @param node Il nodo NodeDecl da visitare
	 */
	public abstract void visit(NodeDecl node);
	
	/**
	 * Visita un nodo che rappresenta un'operazione matematica binaria 
	 * (addizione, sottrazione, moltiplicazione, divisione)
	 * 
	 * @param node Il nodo NodeBinOp da visitare
	 */
	public abstract void visit(NodeBinOp node);
	
	/**
	 * Visita un nodo di assegnamento di un valore a una variabile esistente
	 * 
	 * @param node Il nodo NodeAssign da visitare
	 */
	public abstract void visit(NodeAssign node);
	
	/**
	 * Visita un nodo che rappresenta un'istruzione di stampa su schermo
	 * 
	 * @param node Il nodo NodePrint da visitare
	 */
	public abstract void visit(NodePrint node);
	
	/**
	 * Visita un nodo di dereferenziazione (la lettura del valore di una 
	 * variabile per usarlo all'interno di un'espressione)
	 * 
	 * @param node Il nodo NodeDeref da visitare
	 */
	public abstract void visit(NodeDeref node);
	
	/**
	 * Visita un nodo che rappresenta una costante numerica (es. un numero intero o float)
	 * 
	 * @param node Il nodo NodeCost da visitare
	 */
	public abstract void visit(NodeCost node);
}