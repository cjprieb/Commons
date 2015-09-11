package com.purplecat.commons.xmltests;

import org.dom4j.Node;

public class Difference {
	public final DifferenceType _type;
	public final Node _controlNode;
	public final Node _testNode;
	
	Difference(DifferenceType type, Node node1, Node node2, boolean isFirstPass) {
		_type = type;
		_controlNode = isFirstPass ? node1 : node2;
		_testNode = isFirstPass ? node2 : node1;
	}
	
	@Override
	public String toString() {
		if ( _controlNode != null ) {
			return(String.format("Difference: %s - %s (%s) ctrl", _type, _controlNode.getName(), _controlNode.getText().trim()));
		}
		else if ( _testNode != null ) {
			return(String.format("Difference: %s - %s (%s) test", _type, _testNode.getName(), _testNode.getText().trim()));			
		}
		else {
			return(String.format("Difference: %s", _type));				
		}
	}

}
