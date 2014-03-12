package nl.rabobank.online.readyfortwoclicks.visitor;

import java.util.ArrayList;
import java.util.List;

import nl.rabobank.online.readyfortwoclicks.Node;

public class SearchFirstOccurenceOnTagNodeVisitor implements NodeVisitorI {
	private List<String> toBeMatchedTags = new ArrayList<String>();
	private Node found = null;
	
	public SearchFirstOccurenceOnTagNodeVisitor( String... tags ) {
		for( String tag: tags ) {
			this.toBeMatchedTags.add( tag );
		}
	}
	
	@Override
	public void visit(Node node) {
		
		if( this.found == null && node.containsOneOfTag(toBeMatchedTags)) {
			this.found = node;
		}
	}
	
	public Node getFound() {
		return 	this.found;
	}

}
