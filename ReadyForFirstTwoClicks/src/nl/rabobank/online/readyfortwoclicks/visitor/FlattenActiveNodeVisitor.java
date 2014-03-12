package nl.rabobank.online.readyfortwoclicks.visitor;

import java.util.List;

import nl.rabobank.online.readyfortwoclicks.Node;

public class FlattenActiveNodeVisitor implements NodeVisitorI {
	private List<Node> nodes;
	
	public FlattenActiveNodeVisitor( List<Node> nodes ) {
		this.nodes = nodes;
	}
	
	@Override
	public void visit(Node node) {
		if( node.isActive() == true ) {
			nodes.add(node);
		}
	}

}
