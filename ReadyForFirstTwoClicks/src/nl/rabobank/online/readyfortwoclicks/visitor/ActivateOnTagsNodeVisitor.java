package nl.rabobank.online.readyfortwoclicks.visitor;

import java.util.ArrayList;
import java.util.List;

import nl.rabobank.online.readyfortwoclicks.Node;


public class ActivateOnTagsNodeVisitor implements NodeVisitorI {
	private List<String> toBeMatchedTags = new ArrayList<String>();

	public ActivateOnTagsNodeVisitor( String... tags ) {
		for( String tag: tags ) {
			this.toBeMatchedTags.add( tag );
		}
	}
	
	@Override
	public void visit(Node node) {
		if( node.getTags() != null ) {
			if( node.containsOneOfTag(toBeMatchedTags)) {
				
				// mark current node as enabled
				node.setActive(true);
						
				// mark its parents as enabled
				node.activateParents();
			}
		}

	}

}
