package nl.rabobank.online.readyfortwoclicks.visitor;

import nl.rabobank.online.readyfortwoclicks.Node;


public class ActivateOnDepthNodeVisitor implements NodeVisitorI {
	private int maxDepth = 0;

	public ActivateOnDepthNodeVisitor( int maxDepth ) {
		this.maxDepth = maxDepth;
	}
	
	@Override
	public void visit(Node node) {
		if( node.determineDepthUntillRoot() <= maxDepth ) {
			node.setActive(true);
		}
	}

}
