package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.htmldocument.IHtmlNode;
import umbc.ebiquity.kang.htmldocument.IHtmlNode.NodeType;
import umbc.ebiquity.kang.htmldocument.IHtmlPath;
import umbc.ebiquity.kang.htmldocument.impl.StandardHtmlElement;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.ICustomizedHTMLNodeProcessor;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlayBuilder;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IValueTypeResolver;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.ValueType;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.ValueTypeInfo;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.StandardValueTypeResolver;
import umbc.ebiquity.kang.websiteparser.impl.HTMLTags;

public class HTMLTreeOverlayConstructor implements IHTMLTreeOverlayBuilder {

	private IValueTypeResolver valueTypeResolver;
	private Set<ICustomizedHTMLNodeProcessor> customerizedHtmlNodeProcessors;
	
	private Map<String, IHTMLTreeNode> path2ContextNode;
	private Map<String, List<IHTMLTreeNode>> parent2ContextNodes;
	private Set<String> pathsToSkip;
	private IHTMLTreeNode root;
	private boolean constructed;
	private String domainName;

	public HTMLTreeOverlayConstructor() {
		valueTypeResolver = new StandardValueTypeResolver();
		customerizedHtmlNodeProcessors = new HashSet<ICustomizedHTMLNodeProcessor>();
		init();
	}

	public void setValueTypeResolver(IValueTypeResolver valueTypeResolver) {
		this.valueTypeResolver = valueTypeResolver;
	}
	
	public void registerCustomizedHtmlNodeProcessors(ICustomizedHTMLNodeProcessor customerizedHtmlNodeProcessor){
		customerizedHtmlNodeProcessors.add(customerizedHtmlNodeProcessor);
	}

	@Override
	public IHTMLTreeOverlay build(IHtmlDocumentParsedPathsHolder webPagePathHolder) {
		if (isConstructed()) {
			reset();
		}
		root = createRoot();
		int currPathIndex = 0;
		doConstruct(root, webPagePathHolder.listHtmlPaths(), currPathIndex);
		setConstructed(true);
		domainName = webPagePathHolder.getDomainName();
		return new HTMLTreeOverlay(root, webPagePathHolder.getUniqueIdentifier(), domainName);
	}

	private void reset() {
		path2ContextNode.clear();
		parent2ContextNodes.clear();
		pathsToSkip.clear();
	}

	private void init() {
		path2ContextNode = new HashMap<String, IHTMLTreeNode>();
		parent2ContextNodes = new HashMap<String, List<IHTMLTreeNode>>();
		pathsToSkip = new HashSet<String>();
	}
	
	private IHTMLTreeNode createRoot() {
		HTMLTreeBlankNode rootNode = new HTMLTreeBlankNode("Root");
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
	private void doConstruct(IHTMLTreeNode contextNode, List<IHtmlPath> paths, int currPathIndex) {
		registerContextNode(contextNode);

		// Reaches the bottom of the path list
		if (currPathIndex >= paths.size())
			return;

		// Get current path we are traversing
		IHtmlPath currPath = paths.get(currPathIndex);
		
		System.out.println(currPath.getPathIdent());

		if (toBeSkipped(currPath)) {
			doConstruct(contextNode, paths, ++currPathIndex);
		} else {
			
			// Get the last node (i.e., leaf node) of current path
			IHtmlNode currentNode = currPath.getLastNode();

			//
			int prePathIndex = currPathIndex - 1;
			if (prePathIndex >= 0) {
				IHtmlPath prePath = paths.get(prePathIndex);
				if (shouldBackTracking(currPath, prePath)) {
					String pathID = currentNode.getPrefixPathID();
					IHTMLTreeNode existingContextNode = path2ContextNode.get(pathID);
					if (existingContextNode == null) {
						contextNode = root;
					} else {
						contextNode = existingContextNode;
						List<IHTMLTreeNode> contextNodes = parent2ContextNodes.get(pathID);
						if (!contextNodes.isEmpty()) {
							IHTMLTreeNode candidateContextNode = contextNodes.get(contextNodes.size() - 1);
							if (isEntityNode(currentNode)) {
								//
								IHTMLTreeNode entityNode = createEntityNode(currentNode, currPath);
								contextNode = adjustContext(entityNode, candidateContextNode);
							} else if (isBlankNode(currentNode)) {
								IHTMLTreeNode entityNode = createBlankNode(currentNode, currPath);
								contextNode = adjustContext(entityNode, candidateContextNode);
							} else if (isValueNode(currentNode)) {
								IHTMLTreeNode entityNode = createValueNode(currentNode, currPath);
								contextNode = adjustContext(entityNode, candidateContextNode);
							}
						}
					}
				}
			}

			if (isValueNode(currentNode)) {
				// If the current node is a value node that we defined, this
				// node is a leaf node of current Entity Graph we are
				// constructing and it has no child

				IHTMLTreeNode valueNode = createValueNode(currentNode, currPath);
				if (valueNode != null) {
					contextNode.addChild(valueNode);
				}

				// We are going to traverse the next path and create the next
				// node. The contextNode is the context node of next node
				doConstruct(contextNode, paths, ++currPathIndex);

			} else if (isElementNode(currentNode)) {
				// The node can be EntityNode or BlankNode (i.e., bnode)

				if (isEntityNode(currentNode)) {
					// If the current node is an entity node that we defined,
					// this
					// node is an intermediate node that has child nodes (in
					// rare
					// cases, it may have no child)
					IHTMLTreeNode entityNode = createEntityNode(currentNode, currPath);
					contextNode = adjustContext(entityNode, contextNode);

					contextNode.addChild(entityNode);
					doConstruct(entityNode, paths, ++currPathIndex);

					// INode lastChild = getLastChild(contextNode);
					// if (withHeaderTag(entityNode)) {
					// // If the node is an entity node with heading tag, this
					// node
					// // is the context node of forthcoming node until reaching
					// // the boundary.
					//
					// contextNode.addChild(entityNode);
					// doConstruct(entityNode, paths, ++currPathIndex);
					// } else if (lastChild == null) {
					// // If the current context node has no last child (i.e.,
					// // has no child), we just simply consider the node is a
					// // child of the context node. Then, we continuingly
					// // traverse the paths and consider the node as the
					// // context node for the forthcoming nodes.
					//
					// contextNode.addChild(entityNode);
					// doConstruct(entityNode, paths, ++currPathIndex);
					// } else {
					// if (canBeChild(entityNode, lastChild)) {
					// // If the context has child nodes, we take the last
					// // one and check if the node can be applied as a
					// // child of the last child of the context node. If
					// // yes, we consider the node as the child of the
					// // last child of the context node. Then, we
					// // continuingly traverse the paths and take the node
					// // as the context node.
					//
					// lastChild.addChild(entityNode);
					// doConstruct(entityNode, paths, ++currPathIndex);
					// } else {
					// // Otherwise, we consider the node as child of the
					// // context node. Then, we continuingly traverse the
					// // paths and take the node as the context node.
					//
					// contextNode.addChild(entityNode);
					// doConstruct(entityNode, paths, ++currPathIndex);
					// }
					// }

				} else if (isBlankNode(currentNode)) {
					// The blank node contains no semantic information directly.
					// It serves as a structural element that encompasses
					// related data.Therefore, we consider the blank node as the
					// context node of data it encompasses.

					IHTMLTreeNode bnode = createBlankNode(currentNode, currPath);
					bnode = customizedProcess(bnode, currentNode, currPath, currPathIndex);
					if (bnode == null) {
						doConstruct(contextNode, paths, ++currPathIndex);
					} else {
						contextNode.addChild(bnode);
						doConstruct(bnode, paths, ++currPathIndex);
					}

				} else {
					// Skip current path
					doConstruct(contextNode, paths, ++currPathIndex);
				}

			} else {
				// Skip current path
				doConstruct(contextNode, paths, ++currPathIndex);
			}
		}
	}

	private boolean toBeSkipped(IHtmlPath currPath) {
		if(pathsToSkip.isEmpty())
			return false;
		
		for (String path : pathsToSkip) {
			if (currPath.getPathIdent().startsWith(path)) {
				return true;
			}
		}
		return false;
	}

	// TODO: add customized process for node
	/**
	 * Process specific node (e.g., table or dl) based on specific rules.
	 * 
	 * @param bnode
	 * @param currentNode
	 * @param currentPath
	 * @param currPathIndex
	 * @return
	 */
	private IHTMLTreeNode customizedProcess(IHTMLTreeNode bnode, IHtmlNode currentNode, IHtmlPath currentPath, int currPathIndex) {

		for (ICustomizedHTMLNodeProcessor processor : customerizedHtmlNodeProcessors) {
			StandardHtmlElement htmlElement = new StandardHtmlElement(currentNode.getWrappedElement(),
					currentPath.getPathIdent(), domainName);
			if (processor.isMatched(htmlElement)) {
				bnode = processor.process(htmlElement);
				// record the path to be skipped. All the descendants of this
				// path should be skipped
				pathsToSkip.add(currentPath.getPathIdent());
				break;
			}
		}
		return bnode;
	}

	private void registerContextNode(IHTMLTreeNode contextNode) {
		String pathID = contextNode.getPathID();
		if (path2ContextNode.containsKey(pathID))
			return;
		
		//
		path2ContextNode.put(pathID, contextNode);
		
		//
		String parentPathID = contextNode.getParentPathID();
		List<IHTMLTreeNode> contextNodes = parent2ContextNodes.get(parentPathID);
		if (contextNodes == null) {
			contextNodes = new ArrayList<IHTMLTreeNode>();
			parent2ContextNodes.put(parentPathID, contextNodes);
		} 
		contextNodes.add(contextNode);
	}
	
	private IHTMLTreeNode adjustContext(IHTMLTreeNode currentNode, IHTMLTreeNode contextNode) {
		while (atSameLevel(currentNode, contextNode) && !canBeChild(currentNode, contextNode)) {
			contextNode = contextNode.getParent();
		}
		return contextNode;
	}
	
	private boolean atSameLevel(IHTMLTreeNode currentNode, IHTMLTreeNode contextNode) {
		return currentNode.getParentPathID().equals(contextNode.getParentPathID());
	}

	/**
	 * 
	 * @param currentPath
	 * @param previousPath
	 * @return
	 */
	private boolean shouldBackTracking(IHtmlPath currentPath, IHtmlPath previousPath) {
		return (!currentPath.getPathIdent().startsWith(previousPath.getPathIdent())
				&& !currentPath.getLastNode().getPrefixPathID().equals(previousPath.getLastNode().getPrefixPathID()));
	}

	private IHTMLTreeNode createValueNode(IHtmlNode currentNode, IHtmlPath currentPath) {

		String content = currentNode.getFullContent();
		Element element = currentNode.getWrappedElement();

		// First check whether we can determine value through the element
		// tagName;
		// Then check whether we can determine value through the string content
		// of the HTML node;
		// Finally, check Whether we can determine value through the content of
		// the element.
		HTMLTreeNodeValue treeNodeValue = resolveValueTypeByTagName(element);
		if (treeNodeValue != null) {
			// if the value is not null, the element must not be null;
			HTMLTreeValueNode valueNode = new HTMLTreeValueNode(treeNodeValue, element);
			valueNode.addValues(getValues(element));
			populatePathIdInformation(valueNode, currentPath);
			return valueNode;
		} else if (isNotEmpty(content)) {

			ValueTypeInfo valueType = valueTypeResolver.resolve(content);
			System.out.println(content + ", " + valueType.getUnit());
			treeNodeValue = new HTMLTreeNodeValue(content, valueType);
			HTMLTreeValueNode valueNode = null;
			if (element != null) {
				valueNode = new HTMLTreeValueNode(treeNodeValue, element);
				valueNode.addValues(getValues(element));
			} else {
				valueNode = new HTMLTreeValueNode(treeNodeValue, currentNode.getTag());
			}
			populatePathIdInformation(valueNode, currentPath);

			return valueNode;
		} else if (element != null) {
			List<HTMLTreeNodeValue> values = getValues(element);
			if (!values.isEmpty()) {
				HTMLTreeValueNode valueNode = new HTMLTreeValueNode(values.get(0), element);
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

	private List<HTMLTreeNodeValue> getValues(Element element) {
		List<HTMLTreeNodeValue> values = new ArrayList<HTMLTreeNodeValue>();
		Elements elements = element.getElementsByTag("img");
		String key = "src";
		for (Element e : elements) {
			if (e.hasAttr(key)) {
				HTMLTreeNodeValue value = new HTMLTreeNodeValue(e.attr(key),
						ValueTypeInfo.createValueTypeInfo(ValueType.Image));
				// TODO: get description of image
				// value.setDescription(getImgDescription(e));
				values.add(value);
			}
		}
		return values;
	}

	private HTMLTreeNodeValue resolveValueTypeByTagName(Element element) {
		if (element == null)
			return null;

		if (element.tagName().equalsIgnoreCase("img")) {
			HTMLTreeNodeValue value = new HTMLTreeNodeValue(element.attr("src"),
					ValueTypeInfo.createValueTypeInfo(ValueType.Image));
			return value;
		}

		return null;
	}

	private IHTMLTreeNode createEntityNode(IHtmlNode currentNode, IHtmlPath currentPath) {
		String content = currentNode.isLeafNode() ? currentNode.getFullContent() : "";
		HTMLTreeEntityNode entityNode = new HTMLTreeEntityNode(currentNode.getWrappedElement(), content);
		populatePathIdInformation(entityNode, currentPath);
		return entityNode;
	}

	private IHTMLTreeNode createBlankNode(IHtmlNode node, IHtmlPath currentPath) {
		HTMLTreeBlankNode blankNode = new HTMLTreeBlankNode(node.getWrappedElement());
		populatePathIdInformation(blankNode, currentPath);
		return blankNode;
	}
	
	private void populatePathIdInformation(AbstractHTMLTreeNode node, IHtmlPath currentPath) {
		node.setPathID(currentPath.getPathIdent());
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
	private IHTMLTreeNode getLastChild(IHTMLTreeNode node) {
		List<IHTMLTreeNode> children = node.getChildren();
		int size = children.size();
		if (size == 0) {
			return null;
		} else {
			return children.get(size - 1);
		}
	}

	public boolean isElementNode(IHtmlNode node) {
		return NodeType.ElementNode == node.getNodeType();
	}

	public boolean isValueNode(IHtmlNode node) {
		return NodeType.TextNode == node.getNodeType()
				|| NodeType.ValueElementNode == node.getNodeType();
	}

	private boolean isEntityNode(IHtmlNode node) {
		return HTMLTags.isTopicTag(node.getTag());
	}

	private boolean isBlankNode(IHtmlNode node) {
		return !HTMLTags.isTopicTag(node.getTag()) && !HTMLTags.isValueTag(node.getTag());
	}
	
	private boolean withHeaderTag(IHTMLTreeNode node) {
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
	private boolean canBeChild(IHTMLTreeNode candidateChild, IHTMLTreeNode candidateParent) {
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
}
