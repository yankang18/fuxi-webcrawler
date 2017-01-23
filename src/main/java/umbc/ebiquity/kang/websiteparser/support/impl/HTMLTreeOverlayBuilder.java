package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.htmldocument.IHtmlDocumentNode;
import umbc.ebiquity.kang.htmldocument.IHtmlDocumentNode.DocumentNodeType;
import umbc.ebiquity.kang.htmldocument.IHtmlDocumentPath;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.impl.HTMLTags;
import umbc.ebiquity.kang.websiteparser.support.IHTMLTreeOverlay;
import umbc.ebiquity.kang.websiteparser.support.IHTMLTreeOverlayBuilder;
import umbc.ebiquity.kang.websiteparser.support.IValueTypeResolver;
import umbc.ebiquity.kang.websiteparser.support.impl.Value.ValueType;

public class HTMLTreeOverlayBuilder implements IHTMLTreeOverlayBuilder {

	private IValueTypeResolver valueTypeResolver;
	private Map<String, INode> path2ContextNode;
	private Map<String, List<INode>> parent2ContextNodes;
	private INode root;
	private boolean constructed;

	public HTMLTreeOverlayBuilder() {
		this.valueTypeResolver = new NaiveValueTypeResolver();
	}

	public void setValueTypeResolver(IValueTypeResolver valueTypeResolver) {
		this.valueTypeResolver = valueTypeResolver;
	}

	@Override
	public IHTMLTreeOverlay build(IHtmlDocumentParsedPathsHolder webPagePathHolder) {
		if (isConstructed()) {
			reset();
		} else {
			init();
		}
		
		root = createRoot();
		int currPathIndex = 0;
		doConstruct(root, webPagePathHolder.listHtmlDocumentPaths(), currPathIndex);
		setConstructed(true);
		return new HTMLTreeOverlay(root, webPagePathHolder.getUniqueIdentifier());
	}

	private void reset() {
		path2ContextNode.clear();
		parent2ContextNodes.clear();
	}

	private void init() {
		path2ContextNode = new HashMap<String, INode>();
		parent2ContextNodes = new HashMap<String, List<INode>>();
	}
	
	private RootNode createRoot() {
		RootNode rootNode = new RootNode();
		rootNode.setPathID("Root/");
		rootNode.setParentPathID("/");
		return rootNode;
	}

	/**
	 * 
	 * @param contextNode
	 * @param paths
	 * @param currPathIndex
	 */
	private void doConstruct(INode contextNode, List<IHtmlDocumentPath> paths, int currPathIndex) {
		registerContextNode(contextNode);
		
		if (currPathIndex >= paths.size())
			return;

		// Get current path we are traversing
		IHtmlDocumentPath currPath = paths.get(currPathIndex);
		// Get the last node (i.e., leaf node) of current path
		IHtmlDocumentNode currentNode = currPath.getLastNode();
		
		//
		int prePathIndex = currPathIndex - 1;
		if (prePathIndex >= 0) {
			IHtmlDocumentPath prePath = paths.get(prePathIndex);
			if(shouldBackTracking(currPath, prePath)){
				String pathID = currentNode.getPrefixPathID();
				INode existingContextNode = path2ContextNode.get(pathID);
				if(existingContextNode == null) {
					contextNode = root;
				} else {
					contextNode = existingContextNode;
					List<INode> contextNodes = parent2ContextNodes.get(pathID);
					if (!contextNodes.isEmpty()) {
						INode candidateContextNode = contextNodes.get(contextNodes.size() - 1);
						if (isEntityNode(currentNode)) {
							//
							INode entityNode = createEntityNode(currentNode, currPath);
							contextNode = adjustContext(entityNode, candidateContextNode); 
						} else if(isBlankNode(currentNode)){
							INode entityNode = createBlankNode(currentNode, currPath);
							contextNode = adjustContext(entityNode, candidateContextNode); 
						} else if (isValueNode(currentNode)){
							INode entityNode = createValueNode(currentNode, currPath);
							contextNode = adjustContext(entityNode, candidateContextNode); 
						}
					}
				}
			} 
		} 
		
		if (isValueNode(currentNode)) {
			// If the current node is a value node that we defined, this node is
			// a leaf node of current Entity Graph we are constructing and it
			// has no child
			
			INode valueNode = createValueNode(currentNode, currPath);
			if (valueNode != null) {
				contextNode.addChild(valueNode);
			}

			// We are going to traverse the next path and create the next node.
			// The contextNode is the context node of next node
			doConstruct(contextNode, paths, ++currPathIndex);
			
			
		} else if (isElementNode(currentNode)) {
			// The node can be EntityNode or BlankNode (i.e., bnode)

			if (isEntityNode(currentNode)) {
				// If the current node is an entity node that we defined, this
				// node is an intermediate node that has child nodes (in rare
				// cases, it may have no child)
				INode entityNode = createEntityNode(currentNode, currPath);
				contextNode = adjustContext(entityNode, contextNode); 
				
				contextNode.addChild(entityNode);
				doConstruct(entityNode, paths, ++currPathIndex);
				
//				INode lastChild = getLastChild(contextNode);
//				if (withHeaderTag(entityNode)) {
//					// If the node is an entity node with heading tag, this node
//					// is the context node of forthcoming node until reaching
//					// the boundary.
//					
//					contextNode.addChild(entityNode);
//					doConstruct(entityNode, paths, ++currPathIndex);
//				} else if (lastChild == null) {
//					// If the current context node has no last child (i.e.,
//					// has no child), we just simply consider the node is a
//					// child of the context node. Then, we continuingly
//					// traverse the paths and consider the node as the
//					// context node for the forthcoming nodes.
//
//					contextNode.addChild(entityNode);
//					doConstruct(entityNode, paths, ++currPathIndex);
//				} else {
//					if (canBeChild(entityNode, lastChild)) {
//						// If the context has child nodes, we take the last
//						// one and check if the node can be applied as a
//						// child of the last child of the context node. If
//						// yes, we consider the node as the child of the
//						// last child of the context node. Then, we
//						// continuingly traverse the paths and take the node
//						// as the context node.
//
//						lastChild.addChild(entityNode);
//						doConstruct(entityNode, paths, ++currPathIndex);
//					} else {
//						// Otherwise, we consider the node as child of the
//						// context node. Then, we continuingly traverse the
//						// paths and take the node as the context node.
//
//						contextNode.addChild(entityNode);
//						doConstruct(entityNode, paths, ++currPathIndex);
//					}
//				}

			} else if (isBlankNode(currentNode)) {
				// The blank node contains no semantic information directly. It
				// serves as a structural element that encompasses related
				// data.Therefore, we consider the blank node as the context
				// node of data it encompasses.
				
				INode bnode = createBlankNode(currentNode, currPath);
				contextNode.addChild(bnode);
				doConstruct(bnode, paths, ++currPathIndex);

			} else {
				// Skip current path
				doConstruct(contextNode, paths, ++currPathIndex);
			}

		} else {
			// Skip current path
			doConstruct(contextNode, paths, ++currPathIndex);
		}

	}

	private void registerContextNode(INode contextNode) {
		String pathID = contextNode.getPathID();
		if (path2ContextNode.containsKey(pathID))
			return;
		
		//
		path2ContextNode.put(pathID, contextNode);
		
		//
		String parentPathID = contextNode.getParentPathID();
		List<INode> contextNodes = parent2ContextNodes.get(parentPathID);
		if (contextNodes == null) {
			contextNodes = new ArrayList<INode>();
			parent2ContextNodes.put(parentPathID, contextNodes);
		} 
		contextNodes.add(contextNode);
	}
	
	private INode adjustContext(INode currentNode, INode contextNode) {
		while (atSameLevel(currentNode, contextNode) && !canBeChild(currentNode, contextNode)) {
			contextNode = contextNode.getParent();
		}
		return contextNode;
	}
	
	private boolean atSameLevel(INode currentNode, INode contextNode) {
		return currentNode.getParentPathID().equals(contextNode.getParentPathID());
	}

	/**
	 * 
	 * @param currentPath
	 * @param previousPath
	 * @return
	 */
	private boolean shouldBackTracking(IHtmlDocumentPath currentPath, IHtmlDocumentPath previousPath) {
		return (!currentPath.getPathID().startsWith(previousPath.getPathID())
				&& !currentPath.getLastNode().getPrefixPathID().equals(previousPath.getLastNode().getPrefixPathID()));
	}

	private INode createValueNode(IHtmlDocumentNode currentNode, IHtmlDocumentPath currentPath) {
		
		String content = currentNode.getFullContent();
		Element element = currentNode.getWrappedElement();
		if (isNotEmpty(content)) {
			ValueType valueType = valueTypeResolver.resolve(content);
			Value value = new Value(content, valueType);
			ValueNode valueNode = null;
			if (element != null) {
				valueNode = new ValueNode(value, element);
				valueNode.addValues(getValues(element));
			} else {
				valueNode = new ValueNode(value, currentNode.getTag());
			}
			populatePathIdInformation(valueNode, currentPath);
			return valueNode;
		} else if (element != null) {
			List<Value> values = getValues(element);
			if(!values.isEmpty()) {
				ValueNode valueNode = new ValueNode(values.get(0), element);
				// valueNode.setMainValue(values.get(0));
				for (int i = 1; i < values.size(); i++) {
					valueNode.addValue(values.get(i));
				}
				populatePathIdInformation(valueNode, currentPath);
				return valueNode;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private List<Value> getValues(Element element) {
		List<Value> values = new ArrayList<Value>();
		Elements elements = element.getElementsByTag("img");
		String key = "src";
		for (Element e : elements) {
			if (e.hasAttr(key)) {
				Value value = new Value(e.attr(key), ValueType.Image);
				values.add(value);
			}
		}
		return values;
	}

	private INode createEntityNode(IHtmlDocumentNode currentNode, IHtmlDocumentPath currentPath) {
		EntityNode entityNode = new EntityNode(currentNode.getFullContent(), currentNode.getWrappedElement());
		populatePathIdInformation(entityNode, currentPath);
		return entityNode;
	}

	private INode createBlankNode(IHtmlDocumentNode node, IHtmlDocumentPath currentPath) {
		BlankNode blankNode = new BlankNode(node.getWrappedElement());
		populatePathIdInformation(blankNode, currentPath);
		return blankNode;
	}
	
	private void populatePathIdInformation(AbstractNode node, IHtmlDocumentPath currentPath) {
		node.setPathID(currentPath.getPathID());
		node.setParentPathID(currentPath.getLastNode().getPrefixPathID());
	}

	/**
	 * Get the last child of the specified node.
	 * 
	 * @param node
	 *            the node whose last child will be return
	 * @return the last child of the specified node. It can be null if the node
	 *         has no child
	 */
	private INode getLastChild(INode node) {
		List<INode> children = node.getChildren();
		int size = children.size();
		if (size == 0) {
			return null;
		} else {
			return children.get(size - 1);
		}
	}

	public boolean isElementNode(IHtmlDocumentNode currentNode) {
		return DocumentNodeType.ElementNode == currentNode.getWebTagNodeType();
	}

	public boolean isValueNode(IHtmlDocumentNode currentNode) {
		return DocumentNodeType.TextNode == currentNode.getWebTagNodeType()
				|| DocumentNodeType.ValueElementNode == currentNode.getWebTagNodeType();
	}

	private boolean isEntityNode(IHtmlDocumentNode node) {
		return HTMLTags.isTopicTag(node.getTag());
	}

	private boolean isBlankNode(IHtmlDocumentNode node) {
		return !HTMLTags.isTopicTag(node.getTag()) && !HTMLTags.isLeafTag(node.getTag());
	}
	
	private boolean withHeaderTag(INode node) {
		return HTMLTags.isHeadingTag(node.getTagName());
	}

	/**
	 * Check if the first node can be child of the second node.
	 * 
	 * @param candidateParent
	 *            the first node
	 * @param candidateChild
	 *            the second node
	 * @return true if the first node can be child of the second node
	 */
	private boolean canBeChild(INode candidateChild, INode candidateParent) {
		String pTagName = candidateParent.getTagName();
		String cTagName = candidateChild.getTagName();
		if (HTMLTags.isHeadingTag(pTagName)) {
			if (HTMLTags.isHeadingTag(cTagName)) {
				// If both nodes have heading tag, whether the first node can be
				// applied as child of the second node depends on the importance
				// of the two heading tags.
				if (HTMLTags.getIntegerForHeadingTag(cTagName) < HTMLTags.getIntegerForHeadingTag(pTagName)) {
					return true;
				}
			} else if (HTMLTags.isTopicTag(cTagName)) {
				return true;
			}
		} else if (HTMLTags.isTopicTag(pTagName)) {
			if (!HTMLTags.isTopicTag(cTagName)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isNotEmpty(String content) {
		if (content != null && !content.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check if this EntityGraph has already been constructed.
	 * 
	 * @return true if this EntityGraph has been constructed
	 */
	private boolean isConstructed() {
		return constructed;
	}

	/**
	 * @param constructed the constructed to set
	 */
	private void setConstructed(boolean constructed) {
		this.constructed = constructed;
	}
	
	/**** For Manual Test Purpose *****/
	
	public static void pettyPrint(INode node) {
		pettyPrint(node, "");
	}
	
	private static void pettyPrint(INode node, String intent) {
		for (INode c : node.getChildren()) {
			String value = null;
			if (c instanceof ValueNode) {
				value = ((ValueNode) c).getMainValue().getValue();
			} else if (c instanceof EntityNode) {
				value = ((EntityNode) c).getName();
			}
			value = value == null ? "" : "(" + value + ")";
			System.out.println(intent + c.getTagName() + " " + value);
			pettyPrint(c, intent + "---");
		}
	}

}
