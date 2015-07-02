package com.purplecat.commons.xmltests;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XmlComparison {
	
	boolean _printLogs = true;

	public Differences findDifferences(File file1, File file2, boolean printLogs) throws DocumentException {		
		SAXReader reader = new SAXReader();
		Document doc1 = reader.read(file1);
		Document doc2 = reader.read(file2);		
		return findDifferences(doc1, doc2, printLogs);
	}

	public Differences findDifferences(Document doc1, Document doc2, boolean printLogs) throws DocumentException {
		_printLogs = printLogs;
		Differences _differences = new Differences();

		//only the root element should be compared for same name
		Element root1 = doc1.getRootElement();
		Element root2 = doc2.getRootElement();
		if ( !root1.getName().equalsIgnoreCase(root2.getName()) ) {
			_differences.add(new Difference(DifferenceType.TEXT, root1, root2, true));
		}
		compareNodes(_differences, root1, root2, true, 0);
		
		compareNodes(_differences, root2, root1, false, 0);
		
		return _differences;
	}
	
	private void compareNodes(Differences differences, Element node1, Element node2, boolean isFirstPass, int depth) {
		log(depth, "examining: " + node1.getName() + " (elements? " + node1.elements().size() + ")");
		compareAttributes(differences, node1, node2, isFirstPass, depth);
		if ( node1.elements().size() == 0 && node2.elements().size() == 0 ) {
			if ( isFirstPass && !node1.getTextTrim().equals(node2.getTextTrim()) ) {
				differences.add(new Difference(DifferenceType.TEXT, node1, node2, isFirstPass));
			}
		}
		else if ( node1.elements().size() == 0 ) {
			differences.add(new Difference(DifferenceType.UNKNOWN, node1, node2, isFirstPass));
		}
		else {
			Set<String> pathsParsed = new HashSet<String>();
			for ( Iterator i = node1.elementIterator(); i.hasNext(); ) {
				Element element = (Element)i.next();
				String path = "./" + element.getName();
				log(depth, "examining: " + element.getName() + " at path " + path);
				if ( !pathsParsed.contains(path) ) {
					List nodes1 = node1.selectNodes(path);
					List nodes2 = node2.selectNodes(path);
					
					compareNodeList(differences, nodes1, nodes2, path, isFirstPass, depth);
					pathsParsed.add(path);
				}
			}
		}
	}
	
	private void compareNodeList(Differences differences, List nodes1, List nodes2, String path, boolean isFirstPass, int depth) {
		for ( int i = 0; i < nodes1.size(); i++ ) {
			Element child1 = (Element)nodes1.get(i);
			if ( i < nodes2.size() ) {
				Element child2 = (Element)nodes2.get(i);
				compareNodes(differences, child1, child2, isFirstPass, depth+1);				
			}
			else {
				DifferenceType type = isFirstPass ? DifferenceType.NODE_REMOVED : DifferenceType.NODE_ADDED;	
				differences.add(new Difference(type, (Node)nodes1.get(i), null, isFirstPass));
			}
		}
	}
	
	private void compareAttributes(Differences differences, Element node1, Element node2, boolean isFirstPass, int depth) {
		log(depth, "examining attributes: " + node1.getName() + " (attributes? " + node1.attributeCount() + ")");
		for ( Iterator i = node1.attributeIterator(); i.hasNext(); ) {
			Node attr1 = (Node)i.next();
			boolean found = false;
			for ( Iterator j = node2.attributeIterator(); j.hasNext(); ) {
				Node attr2 = (Node)j.next();
				if ( attr1.getName().equalsIgnoreCase(attr2.getName()) ) {
					if ( isFirstPass && !attr1.getText().equalsIgnoreCase(attr2.getText()) ) {
						differences.add(new Difference(DifferenceType.TEXT, attr1, attr2, isFirstPass));					
					}
					found = true;
					break;
				}
			}
			if ( !found ) {
				DifferenceType type = isFirstPass ? DifferenceType.NODE_REMOVED : DifferenceType.NODE_ADDED;		
				differences.add(new Difference(type, attr1, null, isFirstPass));					
			}
		}
	}
	
	private void log(int depth, String msg) {
		if ( _printLogs ) {
			if ( depth == 0 ) {
				System.out.println(msg);
			}
			else {
				System.out.println(String.format("%-" + depth*4 + "s\t%s", "", msg));
			}
		}
	}

}
