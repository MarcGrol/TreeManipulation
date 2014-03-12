package nl.rabobank.online.readyfortwoclicks.visitor;

import java.util.ArrayList;
import java.util.List;

import nl.rabobank.online.readyfortwoclicks.Node;

public class SearchAllOnTagsNodeVisitor implements NodeVisitorI {
	private List<Node> found;
	private List<String> toBeMatchedTags = new ArrayList<String>();
	
	public SearchAllOnTagsNodeVisitor( List<Node> found, String... tags ) {
		this.found = found; 
		for( String tag: tags ) {
			this.toBeMatchedTags.add( tag );
		}
	}
	
	@Override
	public void visit(Node node) {
		
		if( node.containsOneOfTag(toBeMatchedTags)) {
			found.add(node);
		}
	}

}
