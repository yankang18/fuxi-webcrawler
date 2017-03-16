package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import java.util.ArrayList;
import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlayRefiner;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNodeEntitilizer;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNodeEntitilizer.EntitilizingBlankNodeResult;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue.ValueType;

public class StandardHTMLTreeBlankNodePruner implements IHTMLTreeOverlayRefiner {

	private IHTMLTreeNodeEntitilizer entitilizer;

	public StandardHTMLTreeBlankNodePruner() {
		this.entitilizer = new StandardBlankNodeEntitilizer();
	}

	public void setNodeEntitilizer(IHTMLTreeNodeEntitilizer entitilizer) {
		this.entitilizer = entitilizer;
	}

	@Override
	public IHTMLTreeOverlay refine(IHTMLTreeOverlay overlay) {
		IHTMLTreeNode root = overlay.getTreeRoot();
		doResolve(root);
//		overlay.setRootNote(root);
		return overlay;
	}

	private HTMLTreeEntityNode doResolve(IHTMLTreeNode currentNode) {
		if (currentNode instanceof HTMLTreeValueNode) {
			// If current node is ValueNode, we will do nothing to this node.
			return null;
		} else {
			List<IHTMLTreeNode> children = currentNode.getChildren();
			
			List<IHTMLTreeNode> replacedToBeNodes = new ArrayList<IHTMLTreeNode>();
			List<HTMLTreeEntityNode> replacingNodes = new ArrayList<HTMLTreeEntityNode>();
			for (IHTMLTreeNode child : children) {
				HTMLTreeEntityNode replacingNode = doResolve(child);
				if (replacingNode != null) {
					replacingNodes.add(replacingNode);
					replacedToBeNodes.add(child);
				}
			}
			replace(replacedToBeNodes, replacingNodes, children);

			if (currentNode instanceof HTMLTreeBlankNode) {
				// determine whether the blank node is entitilizable. If yes,
				// convert this node to Entity Node
				HTMLTreeBlankNode currentBNode = (HTMLTreeBlankNode) currentNode;
				EntitilizingBlankNodeResult result = entitilizer.entitilize(currentBNode);
				if (result.isEntitilizable()) {
					HTMLTreeEntityNode currentENode = result.getEntityNode();
					skip(currentENode.getChildren());
					return currentENode;
				}
			}
			
			skip(children);
			return null;
		}
	}
	
	private void replace(List<IHTMLTreeNode> replacedToBeNodes, List<HTMLTreeEntityNode> replacingNodes, List<IHTMLTreeNode> targetNodeList) { 
		for (int i = 0; i < replacedToBeNodes.size(); i++) {
			int index = targetNodeList.indexOf(replacedToBeNodes.get(i));
			targetNodeList.add(index + 1, replacingNodes.get(i));
			targetNodeList.remove(index);
		}
	}

	private void skip(List<IHTMLTreeNode> children) {
		List<HTMLTreeBlankNode> nodesToBeSkipped = new ArrayList<HTMLTreeBlankNode>();
		for (IHTMLTreeNode child : children) {
			if (child instanceof HTMLTreeBlankNode) {
				HTMLTreeBlankNode bnode = (HTMLTreeBlankNode) child;
				if (bnode.isSkippable()) {
					nodesToBeSkipped.add(bnode);
				}
			}
		}
		doSkip(nodesToBeSkipped, children);
	}
 
	private void doSkip(List<HTMLTreeBlankNode> nodesToBeSkipped, List<IHTMLTreeNode> targetNodeList) {
		for (HTMLTreeBlankNode nodeToBeSkipped : nodesToBeSkipped) {
			reconcileChildrenOfNodeToBeSkipped(nodeToBeSkipped, targetNodeList);
		}
	}

	private void reconcileChildrenOfNodeToBeSkipped(HTMLTreeBlankNode bnodeToBeSkipped, List<IHTMLTreeNode> targetNodeList) {
		int index = targetNodeList.indexOf(bnodeToBeSkipped);
		for (IHTMLTreeNode c : bnodeToBeSkipped.getChildren()) {
			targetNodeList.add(index + 1, c);
		}
		targetNodeList.remove(index);
	}
	
}
