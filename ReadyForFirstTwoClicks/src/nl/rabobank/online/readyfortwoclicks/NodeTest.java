package nl.rabobank.online.readyfortwoclicks;


import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static nl.rabobank.online.readyfortwoclicks.Node.Builder.NodeB;

public class NodeTest {
	private static Node root = null;
	
	@Before
	public void setUp() throws Exception {
		root = 	
			NodeB().uid("francine").name("Francine Arts").tag("Geleen").tags("familie","oma")
				.child(NodeB().uid("eva").name("Eva Berkhout").tags("De Bilt","ouder")
						.child( NodeB().uid("pien").name("Pien Grol").lastModified("2003-02-11").tags("De Bilt","familie","kind","schatje")
									.child(NodeB().uid("tisja").name("Tisja").tag("knuffel").build())
								.build() )
						.child( NodeB().uid("tijl").name("Tijl Grol").lastModified("2005-08-25").tags("De Bilt","familie","kind")
									.child(NodeB().uid("jasu").name("Jasu").tag("knuffel").build())
								.build() )
						.child( NodeB().uid("freek").name("Freek Grol").lastModified("2009-09-01").tags("De Bilt","familie","kind")
									.child(NodeB().uid("muis").name("Muis").tag("knuffel").build())
								.build() )
					    .build())
				.child(NodeB().uid("maartje").name("Maartje Berkhout").tags("Amsterdam","familie","ouder")
						.child( NodeB().uid("malou").name("Malou Danbury").tags("Amsterdam","familie","kind")
									.child(NodeB().uid("knuf").name("Knuf").tag("knuffel").build())
								.build() )
						.child( NodeB().uid("flo").name("Flo Danbury").tags("Amsterdam","familie","kind")
									.child(NodeB().uid("hondje").name("Hondje").tag("knuffel").build())
								.build() )
					    .build())
		        .build();
	}

	@After
	public void tearDown() throws Exception {
		root = null;
	}
	
	@Test
	public void testSearchOnUid() {
		{
			Node found = root.searchOnUid("flo");
			Assert.assertNotNull(found);
			Assert.assertEquals("flo", found.getUid() );
			Assert.assertEquals("Flo Danbury", found.getName() );
		}

		{
			Node found = root.searchOnUid("louis");
			Assert.assertNull(found);
		}
	}

	@Test
	public void testSearchOnTag() {
		{
			List<Node> found = root.searchAllOnTags("oma", "kind");
			Assert.assertNotNull(found);
			Assert.assertEquals(6, found.size() );
		}

		{
			List<Node> found = root.searchAllOnTags("alien", "moron");
			Assert.assertNotNull(found);
			Assert.assertEquals(0, found.size() );
		}
	}
	
	@Test
	public void testFlatten() {
		List<Node> found = root.flatten();
		Assert.assertNotNull(found);
		Assert.assertEquals(13, found.size() );
	}

	@Test
	public void testCopy() {
		Node copy = root.copy();
		Assert.assertNotNull(copy);
		Assert.assertEquals("francine", copy.getUid());
		Assert.assertEquals("Francine Arts", copy.getName());
		Assert.assertTrue(copy.containsTag("Geleen"));
		Assert.assertEquals(13, copy.flatten().size() );
	}
	
	@Test
	public void testActivate() {
		root.activateAll();
		Assert.assertEquals(13, root.countActive() );
	}

	@Test
	public void testDeactivate() {
		root.deactivateAll();
		Assert.assertEquals(0, root.countActive() );
	}

	@Test
	public void testActivateOnTags() {
		{
			Node node = root.deactivateAll().activateOnTags("familie");
			Assert.assertNotNull(node);
			Assert.assertEquals(8, node.countActive() );
		}
		{
			Node node = root.deactivateAll().activateOnTags("ouder");
			Assert.assertNotNull(node);
			Assert.assertEquals(3, node.countActive() );
		}
		{
			Node node = root.deactivateAll().activateOnTags("schatje");
			Assert.assertNotNull(node);
			Assert.assertEquals(3, node.countActive() );
		}
		{
			Node node = root.deactivateAll().activateOnTags("alien");
			Assert.assertNotNull(node);
			Assert.assertEquals(0, node.countActive() );
		}

	}

	@Test
	public void testFilterAndCopyOnTags() {
		{
			Node copy = root.filterAndCopyOnTagsOnly("familie");
			Assert.assertNotNull(copy);
			Assert.assertEquals(8, copy.flatten().size() );
		}
		{
			Node copy = root.filterAndCopyOnTagsOnly("ouder");
			Assert.assertNotNull(copy);
			Assert.assertEquals(3, copy.flatten().size() );
		}
		{
			Node copy = root.filterAndCopyOnTagsOnly("schatje");
			Assert.assertNotNull(copy);
			Assert.assertEquals(3, copy.flatten().size() );
		}
		{
			Node copy = root.filterAndCopyOnTagsOnly("alien");
			Assert.assertNull(copy);
		}

	}

	@Test
	public void testActivateOnLevel() {
		{
			Node node = root.deactivateAll().activateUptoDepth(0);
			Assert.assertNotNull(node);
			Assert.assertEquals(1, node.countActive() );
		}
		{
			Node node = root.deactivateAll().activateUptoDepth(1);
			Assert.assertNotNull(node);
			Assert.assertEquals(3, node.countActive() );
		}
		{
			Node node = root.deactivateAll().activateUptoDepth(2);
			Assert.assertNotNull(node);
			Assert.assertEquals(8, node.countActive() );
		}
		{
			Node node = root.deactivateAll().activateUptoDepth(3);
			Assert.assertNotNull(node);
			Assert.assertEquals(13, node.countActive() );
		}
		{
			Node node = root.deactivateAll().activateUptoDepth(4);
			Assert.assertNotNull(node);
			Assert.assertEquals(13, node.countActive() );
		}
	}
	@Test
	public void testFilterAndCopyOnLevel() {
		{
			Node copy = root.filterAndCopyOnDepthOnly(0);
			Assert.assertNotNull(copy);
			Assert.assertEquals(1, copy.flatten().size() );
		}
		{
			Node copy = root.filterAndCopyOnDepthOnly(1);
			Assert.assertNotNull(copy);
			Assert.assertEquals(3, copy.flatten().size() );
		}
		{
			Node copy = root.filterAndCopyOnDepthOnly(2);
			Assert.assertNotNull(copy);
			Assert.assertEquals(8, copy.flatten().size() );
		}
		{
			Node copy = root.filterAndCopyOnDepthOnly(3);
			Assert.assertNotNull(copy);
			Assert.assertEquals(13, copy.flatten().size() );
		}
		{
			Node copy = root.filterAndCopyOnDepthOnly(10);
			Assert.assertNotNull(copy);
			Assert.assertEquals(13, copy.flatten().size() );
		}
	}

	@Test
	public void testSorting() {
		{
			Node result = root.deactivateAll()
			  .activateUptoDepth(1)
			  .searchOnUid("eva")
			  .activateChildrenLeveled(1)
			  .sortLastModifiedFirst(2)
			  ;
			
			Assert.assertNotNull(result);
			Assert.assertEquals("eva", result.getUid() );
			Assert.assertEquals(2, result.countActiveChilden() );
			Assert.assertEquals("freek", result.firstActiveChild().getUid() );
		}

		{
			Node result = root.deactivateAll()
			  .activateUptoDepth(1)
			  .searchOnUid("eva")
			  .activateChildrenLeveled(1)
			  .sortOnName(1)
			  ;
			
			Assert.assertNotNull(result);
			Assert.assertEquals("eva", result.getUid() );
			Assert.assertEquals(1, result.countActiveChilden() );
			Assert.assertEquals("freek", result.firstActiveChild().getUid() );
		}

	}
	
	@Test
	public void demonstrateThePowerOfTheConcept() {

		{
			System.out.println( "Show complete tree" );
			root.printAll();
		}
		
		{
			System.out.println( "Show untill parents" );
			
			Node result = root.deactivateAll()
							  .activateUptoDepth(1);
			
			result.printActive();
		}
		
		{
			System.out.println( "Show untill parents and pien" );
			
			Node result = root.deactivateAll()
							  .activateUptoDepth(1)
							  .activateOnTags("schatje");
			
			result.printActive();
		}
		
		{
			System.out.println( "Show family in amsterdam" );
			
			Node result = root.deactivateAll()
							  .activateOnTags("Amsterdam")
							  .searchFirstOccurenceOnTags("Amsterdam")
//							  .debugCurrent("This should start with maartje")
							  .activateChildrenLeveled(1); /* prevent poppets to be visible */
			
			result.printActive();
		}
		
		{
			System.out.println( "Show role 'parent' only");
			
			List<Node> nodeList = root.searchAllOnTags("ouder");
			
			Node.printList(nodeList);
		}
		
		{
			System.out.println( "Show eva's children only" );
			
			List<Node> nodeList = root.deactivateAll()
							          .searchOnUid("eva")
							          .getChildren();
			
			Node.printList(nodeList);
		}
	}

}
