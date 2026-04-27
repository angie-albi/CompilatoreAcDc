package visitor;

import ast.NodeAssign;
import ast.NodeBinOp;
import ast.NodeCost;
import ast.NodeDecSt;
import ast.NodeDecl;
import ast.NodeDeref;
import ast.NodeId;
import ast.NodePrint;
import ast.NodeProgram;
import symbolTable.Registri;

public class CodeGeneratorVisitor implements IVisitor {

	private String codiceDc; // mantiene il codice della visita
	private String errors;   // raccoglie i messaggi di errore
	
	/**
	 * Costruttore: inizializza le stringhe vuote
	 */
	public CodeGeneratorVisitor() {
		super();
		this.codiceDc = "";
		this.errors = "";
	}
	
	public String getCodiceDc() {
		return codiceDc;
	}

	public String getErrors() {
		return errors;
	}
	
	/**
	 * Visita il nodo radice del programma
	 * 
	 * Itera su tutte le dichiarazioni e le istruzioni, generando il codice sequenzialmente
	 * Si interrompe immediatamente se viene rilevato un errore (es. registri esauriti)
	 */
	@Override
	public void visit(NodeProgram node) {
		for (NodeDecSt decSt: node.getDecSt()) {
			decSt.accept(this);
			
			if(!this.errors.isEmpty())
				return;
		}
	}

	/**
	 * Visita un identificatore
	 * 
	 * Nella generazione del codice, questo metodo rimane vuoto
	 * Le istruzioni 'dc' per i registri (load/store) vengono generate 
	 * dai nodi genitore (NodeDeref, NodeAssign, NodeDecl) in base al contesto
	 */
	@Override
	public void visit(NodeId node) {
		
	}

	@Override
	public void visit(NodeDecl node) {
		char reg = Registri.newRegister();
		
	}

	@Override
	public void visit(NodeBinOp node) {
		/*
		node.getSx().accept(this);
		String sxCodice = codiceDc;
		node.getDx().accept(this);
		String dxCodice = codiceDc;
		
		if() //controlli di errore
		
		codiceDc = ; //codice dell’espressione binari
		*/
	}


	@Override
	public void visit(NodeAssign node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NodePrint node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NodeDeref node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NodeCost node) {
		// TODO Auto-generated method stub
		
	}

}
