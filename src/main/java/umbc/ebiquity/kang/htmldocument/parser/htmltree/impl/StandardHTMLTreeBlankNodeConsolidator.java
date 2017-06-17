package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import java.util.ArrayList;
import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlayRefiner;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNodeEntitizer;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNodeEntitizer.EntitizingBlankNodeResult;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue.ValueType;

public class StandardHTMLTreeBlankNodeConsolidator implements IHTMLTreeOverlayRefiner {

	private IHTMLTreeNodeEntitizer entitizer;

	public StandardHTMLTreeBlankNodeConsolidator() {
		this.entitizer = new StandardBlankNodeEntitizer();
	}

	public void setNodeEntitizer(IHTMLTreeNodeEntitizer entitizer) {
		this.entitizer = entitizer;
	}

	@Override
	public IHTMLTreeOverlay refine(IHTMLTreeOverlay overlay) {
		IHTMLTreeNode root = overlay.getTreeRoot();
		doResolve(root);
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
				// determine whether the blank node is entitizable. If yes,
				// convert this node to Entity Node
				HTMLTreeBlankNode currentBNode = (HTMLTreeBlankNode) currentNode;
				EntitizingBlankNodeResult result = entitizer.entitize(currentBNode);
				if (result.isEntitizable()) {
					HTMLTreeEntityNode currentENode = result.getEntityNode();
					skip(currentENode.getChildren());
					return currentENode;
				}
			}
			
			skip(children);
			return null;
		}
	}
	
	/**
	 * 
	 * @param toBeReplacedNodes
	 * @param replacingNodes
	 * @param targetNodeList
	 */
	private void replace(List<IHTMLTreeNode> toBeReplacedNodes, List<HTMLTreeEntityNode> replacingNodes, List<IHTMLTreeNode> targetNodeList) { 
		for (int i = 0; i < toBeReplacedNodes.size(); i++) {
			int index = targetNodeList.indexOf(toBeReplacedNodes.get(i));
			targetNodeList.add(index + 1, replacingNodes.get(i));
			targetNodeList.remove(index);
		}
	}

	/**
	 * Skips nodes that are skippable.
	 * 
	 * @param nodes
	 *            the list of <code>IHTMLTreeNode</code>s some of which will be
	 *            skipped
	 */
	private void skip(List<IHTMLTreeNode> nodes) {
		List<IHTMLTreeNode> nodesToBeSkipped = new ArrayList<IHTMLTreeNode>();
		for (IHTMLTreeNode node : nodes) {
			if (node instanceof HTMLTreeBlankNode) {
				HTMLTreeBlankNode bnode = (HTMLTreeBlankNode) node;
				if (bnode.isSkippable()) {
					nodesToBeSkipped.add(bnode);
				}
			} else if (node instanceof HTMLTreeEntityNode) {
				HTMLTreeEntityNode enode = (HTMLTreeEntityNode) node;
				if (isEmpty(enode)) {
					nodesToBeSkipped.add(enode);
				}
			}
		}
		doSkip(nodesToBeSkipped, nodes);
	}

	/**
	 * Does the actual work of skipping nodes.
	 * 
	 * @param nodesToBeSkipped
	 *            the list of nodes to be skipped
	 * @param originalNodes
	 *            the list of nodes including nodes in the nodesToBeSkipped.
	 */
	private void doSkip(List<IHTMLTreeNode> nodesToBeSkipped, List<IHTMLTreeNode> originalNodes) {
		for (IHTMLTreeNode nodeToBeSkipped : nodesToBeSkipped) {
			reconcileChildrenOfNodeToBeSkipped(nodeToBeSkipped, originalNodes);
		}
	}

	/**
	 * Skips <code>nodeToBeSkipped</code> from <code>originalNodes</code>.
	 * Specifically, the <code>nodeToBeSkipped</code> will be removed from
	 * <code>originalNodes</code> while the children of
	 * <code>nodeToBeSkipped</code> will be appended to the position of
	 * <code>nodeToBeSkipped</code> in <code>originalNodes</code>.
	 * 
	 * @param nodeToBeSkipped
	 *            the list of nodes to be skipped
	 * @param originalNodes
	 *            the list of nodes including nodes in the nodesToBeSkipped.
	 */
	private void reconcileChildrenOfNodeToBeSkipped(IHTMLTreeNode nodeToBeSkipped, List<IHTMLTreeNode> originalNodes) {
		int index = originalNodes.indexOf(nodeToBeSkipped);
		/*
		 * Appends children of nodeToBeSkipped to the position of
		 * nodeToBeSkipped in originalNodes.
		 */
		for (IHTMLTreeNode c : nodeToBeSkipped.getChildren()) {
			originalNodes.add(index + 1, c);
		}
		originalNodes.remove(index);
	}

	private boolean isEmpty(HTMLTreeEntityNode enode) {
		return enode.getContent() == null || enode.getContent().trim().isEmpty();
	}
}
