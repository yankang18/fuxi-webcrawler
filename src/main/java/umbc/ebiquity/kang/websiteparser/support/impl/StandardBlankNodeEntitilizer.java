package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.List;

import umbc.ebiquity.kang.websiteparser.support.INodeEntitilizer;
import umbc.ebiquity.kang.websiteparser.support.impl.Value.ValueType;

public class StandardBlankNodeEntitilizer implements INodeEntitilizer {

	public EntitilizingBlankNodeResult entitilize(BlankNode blankNode) {

		EntitilizingBlankNodeResult result = new EntitilizingBlankNodeResult();
		result.setEntitilizable(false);
		List<INode> children = blankNode.getChildren();
		int numOfChildren = children.size();
		if (numOfChildren == 0 || numOfChildren == 1)
			return result;

		INode firstNode = children.get(0);
		Value mainValue = null;
		;
		if (firstNode instanceof ValueNode) {
			ValueNode firstValueNode = (ValueNode) firstNode;
			Value value = firstValueNode.getMainValue();
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
			INode node = children.get(i);
			if (node instanceof ValueNode) {
				ValueNode valueNode = (ValueNode) node;
				Value value = valueNode.getMainValue();
				if (!isComparativeDependentToTermType(value.getValueType())) {
					allDependentValues = false;
					break;
				}
			} else if (node instanceof EntityNode) {
				allDependentValues = false;
				break;
			}
		}

		if (!allDependentValues) {
			return result;
		}

		EntityNode entityNode = new EntityNode(mainValue.getValue(), blankNode.getWrappedElement());

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
