package ast;

public class NodeDecl extends NodeDecSt {
	private NodeId id;
	private LangType tipo;
	private NodeExpr init;
	
	public NodeDecl(NodeId id, LangType tipo, NodeExpr init) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.init = init;
	}
	
	public NodeId getId() {
		return id;
	}
	
	public LangType getTipo() {
		return tipo;
	}
	
	public NodeExpr getInit() {
		return init;
	}
	
	@Override
	public String toString() {
		return "<Decl,id=" + id + ",tipo=" + tipo + ",init=" + init + ">";
	}
}
