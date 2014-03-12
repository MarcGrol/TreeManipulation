package nl.rabobank.online.readyfortwoclicks.visitor;

import nl.rabobank.online.readyfortwoclicks.Node;

public class DeactivateAllChildrenNodeVisitor implements NodeVisitorI {

	@Override
	public void visit(Node node) {
		
		node.setActive( false );
		
	}
	
}
