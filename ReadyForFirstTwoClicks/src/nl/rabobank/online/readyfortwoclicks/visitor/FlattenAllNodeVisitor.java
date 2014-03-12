package nl.rabobank.online.readyfortwoclicks.visitor;

import java.util.List;

import nl.rabobank.online.readyfortwoclicks.Node;

public class FlattenAllNodeVisitor implements NodeVisitorI {
	private List<Node> nodes;
	
	public FlattenAllNodeVisitor( List<Node> nodes ) {
		this.nodes = nodes;
	}
	
	@Override
	public void visit(Node node) {
		nodes.add(node);
	}

}
