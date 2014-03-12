package nl.rabobank.online.readyfortwoclicks.visitor;

import nl.rabobank.online.readyfortwoclicks.Node;


public class CopyAllNodeVisitor implements NodeVisitorI {
	private Node rootCopy;
	
	@Override
	public void visit(Node existingNode) {
		
		Node copiedNode = new Node(existingNode);
		Node existingParent = existingNode.getParent();
		if( existingParent == null ) {
			rootCopy = copiedNode;
		} else {
			Node copiedParent =  rootCopy.searchOnUid(existingParent.getUid() );
			copiedNode.setParent(copiedParent);
			copiedParent.addChild( copiedNode );
			
		}
	}
	
	public Node getCopy() {
		return this.rootCopy;
	}

}
