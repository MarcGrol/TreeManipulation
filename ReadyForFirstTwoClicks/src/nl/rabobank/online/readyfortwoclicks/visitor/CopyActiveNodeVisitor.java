package nl.rabobank.online.readyfortwoclicks.visitor;

import java.util.ArrayList;
import java.util.List;

import nl.rabobank.online.readyfortwoclicks.Node;


public class CopyActiveNodeVisitor implements NodeVisitorI {
	private Node rootCopy;
	private List<String> tags = new ArrayList<String>();

	public CopyActiveNodeVisitor( String... tags ) {
		for( String tag: tags ) {
			this.tags.add( tag );
		}
	}
	
	@Override
	public void visit(Node existingNode) {
		
		if( existingNode.isActive() == true ) {
			Node copiedNode = new Node(existingNode );
			Node existingParent = existingNode.getParent();
			if( existingParent == null ) {
				rootCopy = copiedNode;
			} else {
				Node copiedParent =  rootCopy.searchOnUid(existingParent.getUid() );
				copiedParent.addChild( copiedNode );
				copiedNode.setParent(copiedParent);
			}
		}
	}
	
	public Node getCopy() {
		return this.rootCopy;
	}

}
