package nl.rabobank.online.readyfortwoclicks.visitor;

import nl.rabobank.online.readyfortwoclicks.Node;

public class PrintActiveNodeVisitor implements NodeVisitorI {

	@Override
	public void visit(Node node) {
		if( node.isActive() == true ) {
			node.printPretty();
		}
	}

}
