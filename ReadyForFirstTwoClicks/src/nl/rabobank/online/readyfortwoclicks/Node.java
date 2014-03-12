package nl.rabobank.online.readyfortwoclicks;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import nl.rabobank.online.readyfortwoclicks.visitor.ActivateChildrenNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.ActivateAllChildrenNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.CopyActiveNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.CopyAllNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.CountActiveNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.DeactivateAllChildrenNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.FlattenAllNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.ActivateOnDepthNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.ActivateOnTagsNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.NodeVisitorI;
import nl.rabobank.online.readyfortwoclicks.visitor.PrintActiveNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.PrintAllNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.SearchAllOnTagsNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.SearchFirstOccurenceOnTagNodeVisitor;
import nl.rabobank.online.readyfortwoclicks.visitor.SearchOnUidNodeVisitor;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class Node  {
	@Expose
	private String uid;
	
	@Expose
	private String name;
	
	@Expose
	private String value;
	
	@Expose
	private String summary;
	
	@Expose
	private String details;
	
	@Expose
	private Integer badge;
	
	@Expose
	private String thumbnailUrl;
	
	@Expose
	private String resourceUrl;
	
	@Expose
	private Date   created;
	
	@Expose
	private Date lastModified;
	
	@Expose
	private List<String> tags;
	
	private Node parent;
	@Expose
	
	private List<Node> children;
	
	@Expose
	private boolean active = false;

	public Node( Node node ) {
		this.active = false;
		this.uid = node.getUid();
		this.name = node.getName();
		this.value = node.getValue();
		this.summary = node.getSummary();
		this.details = node.getDetails();
		this.thumbnailUrl = node.getThumbnailUrl();
		this.badge = node.getBadge();
		this.resourceUrl = node.getResourceUrl();
		this.created = node.getCreated();
		this.lastModified = node.getLastModified();
		if( node.getTags() != null && node.getTags().size() > 0 ) {
			this.tags = new ArrayList<String>();
			for( String tag : node.getTags() ) {
				this.tags.add(tag);
			}
		}
		if( node.getChildren() != null && node.getChildren().size() > 0 ) {
			// Prepare but do not copy yet
			this.children = new ArrayList<Node>();
		}
	}
	
	public String getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String getSummary() {
		return summary;
	}

	public String getDetails() {
		return details;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public Integer getBadge() {
		return badge;
	}

	public Date getCreated() {
		return created;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public boolean containsTag( String toBeMatchedTag ) {
		boolean status = false;

		if( this.tags != null ) {
			for( String tag: this.tags ) {
				if( toBeMatchedTag.equals(tag) == true ) {
					status = true;
					break;
				}
			}
		}
		
		return status;
	}

	public boolean containsOneOfTag( List<String> toBeMatchedTags ) {
		boolean status = false;

		if( this.tags != null ) {
			for( String tag : toBeMatchedTags ) {
				if( this.containsTag(tag) == true ) {
					status = true;
					break;
				}
			}
		}
		
		return status;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent( Node parent) {
		this.parent = parent;
	}

	public void addChild( Node child ) {
		if( this.children == null ) {
			this.children = new ArrayList<Node>();
		}
		this.children.add( child );
	}
	
	public List<Node> getChildren() {
		return children;
	}

	public void setActive( boolean active ) {
		this.active = active;
	}
	
	public boolean isActive( ) {
		return this.active;
	}

	public int determineDepthUntillRoot() {
		int level = 0;
		
		Node parent = this.getParent();
		while( parent != null ) {
			level++;
			parent = parent.getParent();
		}
		
		return level;
	}

	public int determineDepthUntillNodeUid( String uid ) {
		int level = 0;
		
		if( this.getUid().equals(uid) == true ) {
			level = 0;
		} else {
			boolean matched = false;
			
			Node parent = this.getParent();
			while( parent != null ) {
				level++;
				if( parent.getUid() == uid) {
					matched = true;
					break;
				}
				parent = parent.getParent();
			}
			
			if( matched == false ) {
				level = -1;
			}
		}
		
		return level;
	}

	private void accept( NodeVisitorI visitor ) {
		
		Preconditions.checkArgument(visitor != null );
		
		visitor.visit(this);
		if( children != null ) {
			for( Node child: children ) {
				child.accept(visitor);
			}
		}
	}

	public Node copy() {
		
		CopyAllNodeVisitor visitor = new CopyAllNodeVisitor();
		this.accept( visitor );
		
		return visitor.getCopy();
	}
	
	public Node activateParents() {
		Node parent = this.getParent();
		while( parent != null ) {
			parent.setActive(true);
			parent = parent.getParent();
		}
		
		return this;
	}

	public Node activateOnTags( String... tags ) {
		
		this.accept(new ActivateOnTagsNodeVisitor(tags));
		
		return this;
	}
	
	public Node copyActive() {
		
		CopyActiveNodeVisitor visitor = new CopyActiveNodeVisitor();
		this.accept( visitor );
		
		return visitor.getCopy();
	}
	
	public Node filterAndCopyOnTagsOnly( String... tags) {
		
		this.deactivateAll();

		this.activateOnTags(tags);
		
		return copyActive();
	}
	
	public Node activateAll() {

		// Activate
		ActivateAllChildrenNodeVisitor visitor = new ActivateAllChildrenNodeVisitor();
		this.accept(visitor);
		
		return this;
	}

	public Node deactivateAll() {

		// De-activate
		DeactivateAllChildrenNodeVisitor visitor = new DeactivateAllChildrenNodeVisitor();
		this.accept(visitor);
		
		return this;
	}
	
	public int countActiveChilden() {
		int counter = 0;
		
		for( Node child: this.children ) {
			if( child.isActive() == true ) {
				counter++;
			}
		}
		return counter;
	}
	
	public int countActive() {

		// Count active nodes
		CountActiveNodeVisitor visitor = new CountActiveNodeVisitor();
		this.accept(visitor);
		
		return visitor.getCounter();
	}

	public Node countActiveFluent() {

		// Count active nodes
		CountActiveNodeVisitor visitor = new CountActiveNodeVisitor();
		this.accept(visitor);
		
		System.err.println("Active node count:" + visitor.getCounter() );
		
		return this;
	}

	public Node root() {
		Node root = null;
	
		if( this.getParent() == null ) {
			// we are the root
			root = this;
		} else {
			Node parent = this.getParent();
			while( parent != null ) {
				parent = parent.getParent();
			}
			root = parent;
		}
			
		return root;
	}

	public Node activateUptoDepth( int depth ) {
		
		this.accept( new ActivateOnDepthNodeVisitor(depth) );
		
		return this;
	}
	
	public Node activateChildrenLeveled( int depth ) {
		
		this.accept( new ActivateChildrenNodeVisitor(this.getUid(), depth) );
		
		return this;
	}

	public Node filterAndCopyOnDepthOnly( int level) {
		
		Preconditions.checkArgument( level>= 0 );
		
		// Mark all nodes as disabled
		this.deactivateAll();

		this.activateUptoDepth( level );
		
		// copy all enabled nodes to a new tree
		return copyActive();
	}
		
	public List<Node> flatten() {
		List<Node> flattened = new ArrayList<Node>();
		
		this.accept(new FlattenAllNodeVisitor(flattened) );
		
		return flattened;
	}
	
	public Node searchOnUid( String uid ) {
		
		SearchOnUidNodeVisitor visitor = new SearchOnUidNodeVisitor( uid) ;
		this.accept(visitor );

		return visitor.getFound();
	}
	
	public Node searchFirstOccurenceOnTags( String... tags ) {
		
		SearchFirstOccurenceOnTagNodeVisitor visitor = new SearchFirstOccurenceOnTagNodeVisitor(tags);
		this.accept( visitor );
		
		return visitor.getFound();
	}

	public List<Node> searchAllOnTags( String... tags ) {
		List<Node> found = new ArrayList<Node>();
		
		this.accept(new SearchAllOnTagsNodeVisitor( found, tags) );

		return found;
	}

	public Node sortOnName( int limitTo ) {
		
		Collections.sort(this.children, Node.NameAscendingComparator);
				
		 return this.limitChildren(limitTo);
	}
	
	public Node sortLastModifiedFirst( int limitTo ) {
		
		Collections.sort(this.children, Node.LastUpdatedDescendingComparator);
		
		return this.limitChildren(limitTo);
	}
	
	public Node limitChildren( int limitTo ) {
		int idx = 1;
		for( Node child: this.children ) {
			if( idx <= limitTo ) {
				child.setActive(true);
			} else {
				child.setActive(false);
			}
			idx++;
		}
		return this;
	}
	
	public Node firstActiveChild( ) {
		Node found = null;
		for( Node child: this.children ) {
			if( child.isActive() == true ) {
				found = child;
				break;
			}
		}
		return found;
	}

	public static void printList( List<Node> nodeList ) {
		System.out.println( "---------------------------" );
		for( Node node: nodeList ) {
			node.printPretty();
		}
		System.out.println("\n" );
	}
	
	public Node debugCurrent( String debugText ) {
		System.err.println(debugText + ":" + this.toString() );
		return this;
	}
	
	public void printActive( ) {
		System.out.println( "---------------------------" );
		this.accept(new PrintActiveNodeVisitor() );
		System.out.println("\n" );
	}

	public void printAll( ) {
		System.out.println( "---------------------------" );
		this.accept(new PrintAllNodeVisitor() );
		System.out.println("\n" );
	}

	public static Comparator<Node> NameAscendingComparator 
		    = new Comparator<Node>() {
		
		public int compare(Node nodeLeft, Node nodeRight) {
		
			String nodeLeftName = nodeLeft.getName();
			String nodeRightName = nodeRight.getName();
			
			if( nodeLeftName != null ) {
				//ascending order
				return nodeLeftName.compareTo(nodeRightName);
			} else {
				return 0;
			}
		}

	};

	public static Comparator<Node> LastUpdatedDescendingComparator 
		    = new Comparator<Node>() {
		
		public int compare(Node nodeLeft, Node nodeRight) {
			
			Date leftUpdated = nodeLeft.getLastModified();
			Date rightUpdated = nodeRight.getLastModified();
			
			//descending order
			if( rightUpdated != null ) {
				return rightUpdated.compareTo(leftUpdated);
			} else {
				return 0;
			}
			
		}
	
	};

	public static class Builder {
		private String uid;
		private String name;
		private String value;
		private String summary;
		private String details;
		private Integer badge;
		private String thumbnailUrl;
		private String resourceUrl;
		private Date created;
		private Date lastModified;
		private List<String> tags;
		private Node parent;
		private List<Node> children;

		public static Builder NodeB() {
			return new Node.Builder();
		}

		public Builder uid(String uid) {
			this.uid = uid;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder value(String value) {
			this.value = value;
			return this;
		}

		public Builder summary(String summary) {
			this.summary = summary;
			return this;
		}

		public Builder details(String details) {
			this.details = details;
			return this;
		}

		public Builder badge(Integer badge) {
			this.badge = badge;
			return this;
		}

		public Builder thumbnailUrl(String thumbnailUrl) {
			this.thumbnailUrl = thumbnailUrl;
			return this;
		}

		public Builder resourceUrl(String resourceUrl) {
			this.resourceUrl = resourceUrl;
			return this;
		}

		public Builder created( Date created ) {
			this.created = created;
			return this;
		}

		public Builder lastModified( Date lastModified ) {
			this.lastModified = lastModified;
			return this;
		}
		
		public Builder lastModified( String lastModifiedString ) {
			DateFormat df = null;
			try {
			    df = new SimpleDateFormat("yyyy-MM-dd");
			    this.lastModified =  df.parse(lastModifiedString);  
		    } catch (ParseException e) {
			    try {
				    df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			    	this.lastModified =  df.parse(lastModifiedString);  
			    } catch(ParseException e2) {
			    	System.err.println("Error parsing updated time-field " + lastModifiedString );
			    }
			}
			return this;
		}

		public Builder tag(String tag) {
			if( this.tags == null ) {
				this.tags = new ArrayList<String>();
			}
			this.tags.add( tag );
			return this;
		}

		public Builder tags(String... tags) {
			if( this.tags == null ) {
				this.tags = new ArrayList<String>();
			}
			for( String tag: tags ) {
				this.tags.add(tag);
			}
			return this;
		}

		public Builder tags(List<String> tags) {
			this.tags = tags;
			return this;
		}

		public Builder child(Node  child) {
			if( this.children == null ) {
				this.children = new ArrayList<Node>();
			}
			this.children.add( child );
			return this;
		}

		public Builder children(List<Node> childNodes) {
			this.children = childNodes;
			return this;
		}

		public boolean isValid() {
			boolean status = true;
			
			if( this.uid == null ) {
				//System.err.println("Missing mandatory field uid");
				//status = false;
			}
			return status;
		}
		
		public Node build() {
			if( this.isValid() == false ) {
		        throw new IllegalStateException("Error building node");
		    }
			return new Node(this);
		}

	}
	
	private Node(Builder builder) {
		super();
		
		this.uid = builder.uid;
		this.name = builder.name;
		this.value = builder.value;
		this.summary = builder.summary;
		this.details = builder.details;
		this.thumbnailUrl = builder.thumbnailUrl;
		this.badge = builder.badge;
		this.resourceUrl = builder.resourceUrl;
		this.created = builder.created;
		this.lastModified = builder.lastModified;
		this.tags = builder.tags;
		this.parent = builder.parent;
		this.children = builder.children;
		if( builder.children != null ) {
			for( Node child : builder.children ) {
				child.setParent(this);
			}
		}
	}

	public String toJson() {
		Gson gson = new GsonBuilder()
			.excludeFieldsWithoutExposeAnnotation()
			.setPrettyPrinting()
			.create();
		return gson.toJson(this);
	}
	
	public void printPretty( ) {
		for( int i=0; i<this.determineDepthUntillRoot(); i++ ) {
			System.out.print("\t");
		}
		System.out.println( " - uid="+this.getUid() + ", name=" + this.getName() + ", active="+this.isActive());
	}

	@Override
	public String toString() {
		 return Objects.toStringHelper(this)
	         .add("#", this.hashCode())
	         .add("depth", this.determineDepthUntillRoot())
	         .add("uid", this.uid)
	         .add("name", this.name)
	         .add("value", this.value)
	         .add("summary", this.summary)
	         .add("details", this.details)
	         .add("thumbnailUrl", this.thumbnailUrl)
	         .add("badge", this.badge)
	         .add("resourceUrl", this.resourceUrl)
	         .add("timestamp", this.created)
	         .add("updated", this.lastModified)
	         .add("tags",  this.tags == null ? "[]" : this.tags)
	         .add("parent", this.parent == null ? "null" : parent.getUid())
	         .add("childrenCount",  this.children == null ? 0 : this.children.size())
         .toString();	
	}

	
}
