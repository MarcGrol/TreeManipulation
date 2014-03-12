package nl.rabobank.online.readyfortwoclicks.visitor;

import nl.rabobank.online.readyfortwoclicks.Node;

public class SearchOnUidNodeVisitor implements NodeVisitorI {
	private String uid;
	private Node found;
	
	public SearchOnUidNodeVisitor( String uid ) {
		this.uid = uid;
	}
	
	@Override
	public void visit(Node node) {
		if( node.getUid() != null && node.getUid().equals(this.uid)) {
			this.found = node;
		}
	}
	
	public Node getFound() {
		return found;
	}

}
