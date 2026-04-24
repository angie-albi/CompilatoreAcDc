package visitor;

import ast.NodeAssign;
import ast.NodeBinOp;
import ast.NodeCost;
import ast.NodeDecl;
import ast.NodeDeref;
import ast.NodeId;
import ast.NodePrint;
import ast.NodeProgram;

public class CodeGeneratorVisitor implements IVisitor {

	//private String codiceDc; // mantiene il codice della visita
	//private boolean errors;  flag che indica se ci sono errori
	
	@Override
	public void visit(NodeProgram node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NodeId node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NodeDecl node) {
		// TODO Auto-generated method stub
		
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
