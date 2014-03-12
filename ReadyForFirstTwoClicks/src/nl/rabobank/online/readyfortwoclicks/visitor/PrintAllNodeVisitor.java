package nl.rabobank.online.readyfortwoclicks.visitor;

import nl.rabobank.online.readyfortwoclicks.Node;

public class PrintAllNodeVisitor implements NodeVisitorI {

	@Override
	public void visit(Node node) {
		node.printPretty();
	}

}
