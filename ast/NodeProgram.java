package ast;

import java.util.ArrayList;

public class NodeProgram extends NodeAst {

	private ArrayList<NodeDecSt> decSt;

	public NodeProgram(ArrayList<NodeDecSt> decSt) {
		super();
		this.decSt = decSt;
	}
	
	public ArrayList<NodeDecSt> getDecSt() {
		return decSt;
	}

	@Override
	public String toString() {
		return "<Program," + decSt + ">";
	}

}
