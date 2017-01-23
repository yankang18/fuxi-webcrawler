package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.ArrayList;
import java.util.List;

import umbc.ebiquity.kang.websiteparser.support.IBlankNodeResolver;
import umbc.ebiquity.kang.websiteparser.support.IHTMLTreeOverlay;
import umbc.ebiquity.kang.websiteparser.support.INodeEntitilizer;
import umbc.ebiquity.kang.websiteparser.support.INodeEntitilizer.EntitilizingBlankNodeResult;
import umbc.ebiquity.kang.websiteparser.support.impl.Value.ValueType;

public class BlankNodeResolver implements IBlankNodeResolver {

	private INodeEntitilizer entitilizer;

	public BlankNodeResolver() {
		this.entitilizer = new StandardBlankNodeEntitilizer();
	}

	public void setNodeEntitilizer(INodeEntitilizer entitilizer) {
		this.entitilizer = entitilizer;
	}

	@Override
	public IHTMLTreeOverlay resolve(IHTMLTreeOverlay overlay) {
		INode root = overlay.getTreeRoot();
		doResolve(overlay.getTreeRoot());
		overlay.setRootNote(root);
		return overlay;
	}

	private EntityNode doResolve(INode currentNode) {

		if (currentNode instanceof ValueNode) {
			return null;
		} else {
			List<INode> children = currentNode.getChildren();
			
			List<INode> replacedToBeNodes = new ArrayList<INode>();
			List<EntityNode> replacingNodes = new ArrayList<EntityNode>();
			for (INode child : children) {
				EntityNode replacingNode = doResolve(child);
				if (replacingNode != null) {
					replacingNodes.add(replacingNode);
					replacedToBeNodes.add(child);
				}
			}
			replace(replacedToBeNodes, replacingNodes, children);

			if (currentNode instanceof BlankNode) {
				// determine whether the blank node is entitilizable. If yes,
				// convert this node to Entity Node
				BlankNode currentBNode = (BlankNode) currentNode;
				EntitilizingBlankNodeResult result = entitilizer.entitilize(currentBNode);
				if (result.isEntitilizable()) {
					EntityNode currentENode = result.getEntityNode();
					skip(currentENode.getChildren());
					return currentENode;
				}
			}
			
			skip(children);
			return null;
		}
	}
	
	private void replace(List<INode> replacedToBeNodes, List<EntityNode> replacingNodes, List<INode> targetNodeList) { 
		for (int i = 0; i < replacedToBeNodes.size(); i++) {
			int index = targetNodeList.indexOf(replacedToBeNodes.get(i));
			targetNodeList.add(index + 1, replacingNodes.get(i));
			targetNodeList.remove(index);
		}
	}

	private void skip(List<INode> children) {
		List<BlankNode> nodesToBeSkipped = new ArrayList<BlankNode>();
		for (INode child : children) {
			if (child instanceof BlankNode) {
				BlankNode bnode = (BlankNode) child;
				if (bnode.isSkippable()) {
					nodesToBeSkipped.add(bnode);
				}
			}
		}
		doSkip(nodesToBeSkipped, children);
	}
 
	private void doSkip(List<BlankNode> nodesToBeSkipped, List<INode> targetNodeList) {
		for (BlankNode nodeToBeSkipped : nodesToBeSkipped) {
			reconcileChildrenOfNodeToBeSkipped(nodeToBeSkipped, targetNodeList);
		}
	}

	private void reconcileChildrenOfNodeToBeSkipped(BlankNode bnodeToBeSkipped, List<INode> targetNodeList) {
		int index = targetNodeList.indexOf(bnodeToBeSkipped);
		for (INode c : bnodeToBeSkipped.getChildren()) {
			targetNodeList.add(index + 1, c);
		}
		targetNodeList.remove(index);
	}
	
}
