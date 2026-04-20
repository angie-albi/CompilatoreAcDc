package ast;

public class NodeId extends NodeAst{

	private String name;

	public NodeId(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "<Id," + name + ">";
	}	
}
