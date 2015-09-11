package com.purplecat.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Optional;

import org.junit.Test;

import com.purplecat.commons.xmltests.Difference;
import com.purplecat.commons.xmltests.DifferenceType;
import com.purplecat.commons.xmltests.Differences;
import com.purplecat.commons.xmltests.XmlComparison;

public class XmlMatcherTests {
	static final String DIRECTORY = "src/com/purplecat/commons/xmltests/";
	static final String AUSTEN_ADDED = "austen_added.xml";
	static final String AUSTEN_EDIT= "austen_edit.xml";
	static final String AUSTEN_SAME = "austen_same.xml";
	static final String AUSTEN_ORIG = "austen_orig.xml";
	static final String AUSTEN_REORDER = "austen_reorder.xml";

	@Test
	public void ignoreWhitespace() throws Exception {
		XmlComparison matcher = new XmlComparison();
		Differences diff = matcher.findDifferences(
				new File(DIRECTORY, AUSTEN_ORIG),
				new File(DIRECTORY, AUSTEN_SAME),
				true);
		
		assertNotNull(diff);
		assertEquals(0, diff.size());
	}

	@Test
	public void nodesEdited() throws Exception  {
		XmlComparison matcher = new XmlComparison();
		Differences differences = matcher.findDifferences(
				new File(DIRECTORY, AUSTEN_ORIG),
				new File(DIRECTORY, AUSTEN_EDIT),
				true);
		
		assertNotNull(differences);

		print("Nodes Edited Differences:", differences);
		assertEquals(3, differences.size());
		
		check(differences, DifferenceType.TEXT, "firstname");
		check(differences, DifferenceType.TEXT, "title");
		check(differences, DifferenceType.TEXT, "dbid");
	}

	@Test
	public void nodesAdded() throws Exception  {
		XmlComparison matcher = new XmlComparison();
		Differences differences = matcher.findDifferences(
				new File(DIRECTORY, AUSTEN_ORIG),
				new File(DIRECTORY, AUSTEN_ADDED),
				true);
		
		assertNotNull(differences);
		
		print("Nodes Added Differences:", differences);
		assertEquals(3, differences.size());
		
		check(differences, DifferenceType.NODE_ADDED, "website");
		check(differences, DifferenceType.NODE_ADDED, "book");
		check(differences, DifferenceType.NODE_ADDED, "version");
	}

	@Test
	public void nodesRemoved() throws Exception  {
		XmlComparison matcher = new XmlComparison();
		Differences differences = matcher.findDifferences(
				new File(DIRECTORY, AUSTEN_ADDED),
				new File(DIRECTORY, AUSTEN_ORIG),
				true);
		
		assertNotNull(differences);
		
		print("Nodes Removed Differences:", differences);
		assertEquals(3, differences.size());
		
		check(differences, DifferenceType.NODE_REMOVED, "website");
		check(differences, DifferenceType.NODE_REMOVED, "book");
		check(differences, DifferenceType.NODE_REMOVED, "version");
	}

//	@Test
//	public void nodesReordered() throws Exception  {
//		XmlComparison matcher = new XmlComparison();
//		Differences differences = matcher.findDifferences(
//				new File(DIRECTORY, AUSTEN_ORIG),
//				new File(DIRECTORY, AUSTEN_REORDER));
//		
//		assertNotNull(differences);
//		
//		print("Nodes Reordered Differences:", differences);
//		
//		assertEquals(0, differences.size());
//	}
	
	private void print(String msg, Differences differences) {
		System.out.println(msg);
		for ( Difference diff : differences ) {
			System.out.println("\t" + diff);
		}
	}
	
	private void check(Differences differences, DifferenceType type, String nodeName) {
		String debug = nodeName + "-" + type;
		Optional<Difference> diff = differences.stream()
				.filter(m -> m._type == type && 
				( (m._controlNode != null && m._controlNode.getName().equals(nodeName)) ||
						(m._testNode != null && m._testNode.getName().equals(nodeName) ) ) ).findFirst();
		assertTrue("no " + debug + " difference", diff.isPresent());
		if ( type == DifferenceType.TEXT ) {
			assertNotNull("no " + debug + " test node", diff.get()._testNode);
			assertEquals("no " + debug + " text", nodeName, diff.get()._testNode.getName());
		}
	}
}
