package nl.rabobank.online.readyfortwoclicks.visitor;

import nl.rabobank.online.readyfortwoclicks.Node;

public class CountActiveNodeVisitor implements NodeVisitorI {
	private int counter = 0;
	
	@Override
	public void visit(Node node) {
		
		if( node.isActive() == true ) {
			counter++;
		}
	}
	
	public int getCounter() {
		return this.counter;
	}

}
