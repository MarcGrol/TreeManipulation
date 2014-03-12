package nl.rabobank.online.readyfortwoclicks.visitor;

import nl.rabobank.online.readyfortwoclicks.Node;

public class ActivateChildrenNodeVisitor implements NodeVisitorI {
	public String parentUid = null;
	public int depth = 0;
	
	public ActivateChildrenNodeVisitor( String parentUid, int depth ) {
		this.parentUid = parentUid;
		this.depth = depth;
	}
	
	@Override
	public void visit(Node node) {
		
		int depthUntill = node.determineDepthUntillNodeUid(this.parentUid);
		if( depthUntill >= 0 && depthUntill <= this.depth ) {
			node.setActive( true );
		}
		
	}
	
}
