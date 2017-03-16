package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNodeEntitilizer;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue.ValueType;

public class StandardBlankNodeEntitilizer implements IHTMLTreeNodeEntitilizer {

	public EntitilizingBlankNodeResult entitilize(HTMLTreeBlankNode blankNode) {

		EntitilizingBlankNodeResult result = new EntitilizingBlankNodeResult();
		result.setEntitilizable(false);
		List<IHTMLTreeNode> children = blankNode.getChildren();
		int numOfChildren = children.size();
		if (numOfChildren == 0 || numOfChildren == 1)
			return result;

		IHTMLTreeNode firstNode = children.get(0);
		HTMLTreeNodeValue mainValue = null;
		;
		if (firstNode instanceof HTMLTreeValueNode) {
			HTMLTreeValueNode firstValueNode = (HTMLTreeValueNode) firstNode;
			HTMLTreeNodeValue value = firstValueNode.getMainValue();
			if (ValueType.Term == value.getValueType()) {
				mainValue = value;
			} else {
				return result;
			}

		} else {
			return result;
		}

		boolean allDependentValues = true;
		for (int i = 1; i < numOfChildren; i++) {
			IHTMLTreeNode node = children.get(i);
			if (node instanceof HTMLTreeValueNode) {
				HTMLTreeValueNode valueNode = (HTMLTreeValueNode) node;
				HTMLTreeNodeValue value = valueNode.getMainValue();
				if (!isComparativeDependentToTermType(value.getValueType())) {
					allDependentValues = false;
					break;
				}
			} else if (node instanceof HTMLTreeEntityNode) {
				allDependentValues = false;
				break;
			}
		}

		if (!allDependentValues) {
			return result;
		}

		HTMLTreeEntityNode entityNode = new HTMLTreeEntityNode(mainValue.getValue(), blankNode.getWrappedElement());

		for (int i = 1; i < numOfChildren; i++) {
			entityNode.addChild(children.get(i));
		}
		
		entityNode.setPathID(blankNode.getPathID());
		entityNode.setParentPathID(blankNode.getParentPathID());
		entityNode.setParent(blankNode.getParent());
		
		result.setEntityNode(entityNode);
		result.setEntitilizable(true);

		return result;
	}

	private boolean isComparativeDependentToTermType(ValueType valueType) {
		return ValueType.Term == valueType ? false : true;
	}

}
