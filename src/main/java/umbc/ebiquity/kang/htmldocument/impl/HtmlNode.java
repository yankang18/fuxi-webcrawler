package umbc.ebiquity.kang.htmldocument.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.IHtmlNode;
import umbc.ebiquity.kang.textprocessing.util.TextProcessingUtils;
import umbc.ebiquity.kang.websiteparser.impl.HTMLTags;

public class HtmlNode implements IHtmlNode{
	
	private HtmlNode parent;
	private HtmlNode child;
	private HtmlPath residePath;
	
	private NodeType nodeType;
	private String prefixPathID;
	
	private Element element;
	private int tagCount;
	private String textContent;
	private Collection<HtmlNode> children;
	private Collection<HtmlNode> sibling;
	private Map<String, String> attributes;
	private boolean isLeafNode = true;
	
	public HtmlNode(Element node, int tagCount) {
		this.element = node;
		this.tagCount = tagCount;
		this.nodeType = NodeType.ElementNode;
		this.isLeafNode = false;
		this.textContent = "";
		this.init();
	}
	
	public HtmlNode(String textNodeContent) {
		this.nodeType = NodeType.TextNode;
		this.isLeafNode = true;
		this.textContent = textNodeContent;
		this.init();
	}

	/**
	 * You should only call this method on node with element type
	 */
	public void toValueNode() {
		if (this.nodeType == NodeType.ElementNode) {
			this.nodeType = NodeType.ValueElementNode;
			this.textContent = this.element.text().trim();
			this.isLeafNode = true;
		} else {
			throw new IllegalStateException(
					"You can not convert node with type [" + this.nodeType + "] to value node ");
		}
	}
	
	private void setContent(String content){
		this.textContent = content;
	}
	
	private void init(){
		this.prefixPathID = null;
		this.parent =  null;
		this.child = null;
		this.residePath = null;
		this.children = null;
		this.sibling = null;
		this.attributes = null;
	}

	public String getTag() {
		if (nodeType == NodeType.ElementNode || nodeType == NodeType.ValueElementNode) {
			return element.tagName().toLowerCase();
		} else {
			return "text";
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTagCount(){
		return  "(" + this.tagCount + ")";
	}
	
	public String getContent() {
		String textContent;
		if (this.getTag().equals("img")) {
			textContent = this.getHiddenText();
		} else {
			textContent = this.textContent;
		}
		return TextProcessingUtils.escapeSpecial(textContent);
	}

	public String getFullContent() {
		String textContent;
		if (nodeType == NodeType.TextNode) {
			textContent = this.textContent;
		} else if (this.getTag().equals("img")) {
			textContent = this.getHiddenText();
		} else {
			textContent = this.element.text().trim();
		}
		return TextProcessingUtils.escapeSpecial(textContent);
	}
	
	// TODO: should move this to a separate class???
	private String getHiddenText() {
		String elemAlt = element.attr("alt");
		String elemTitle = element.attr("title");
		if (elemAlt != null && !TextProcessingUtils.isStringEmpty(elemAlt)) {
			return elemAlt;
		} else if (elemTitle != null && !TextProcessingUtils.isStringEmpty(elemTitle)) {
			return elemTitle;
		} else {
			// TODO parse the src of the image
		}
		return "";
	}

	public Collection<HtmlNode> listChildren() {
		this.populateChildrenCollection();
		return this.children;
	}

	public Collection<HtmlNode> listSiblings() {
		this.populateSiblingCollection();
		return this.sibling;
	}

	public Map<String, String> listAttributes() {
		this.populateAttributeMap();
		return this.attributes;
	}

	public Set<String> attributeKeySet() {
		this.populateAttributeMap();
		return this.attributes.keySet();
	}

	public String attributeValue(String key) {
		this.populateAttributeMap();
		return this.attributes.get(key);
	}

	private void populateAttributeMap(){
		
		if (this.attributes == null) {
			this.attributes = new LinkedHashMap<String, String>();
			if(nodeType == NodeType.TextNode){
				return;
			}
			for (Attribute attribute : this.element.attributes()) {
				String key = attribute.getKey();
				if (key.equalsIgnoreCase("class") || key.equalsIgnoreCase("id") || key.equalsIgnoreCase("style")) {
					attributes.put(attribute.getKey(), attribute.getValue());
				}
			}
		}
	}
	
	private void populateChildrenCollection() {
		if (this.children == null) {
			this.children = new ArrayList<HtmlNode>();
			if (nodeType == NodeType.TextNode || nodeType == NodeType.ValueElementNode) {
				return;
			}
			for (Element child : this.element.children()) {
				HtmlNode nodeWrapper = new HtmlNode(child, 1);
				this.children.add(nodeWrapper);
			}
		}
	}

	private void populateSiblingCollection() {
		if (this.sibling == null) {
			this.sibling = new ArrayList<HtmlNode>();
			if (nodeType == NodeType.TextNode) {
				return;
			}
			for (Element child : this.element.siblingElements()) {
				HtmlNode nodeWrapper = new HtmlNode(child, 1);
				this.sibling.add(nodeWrapper);
			}
		}
	}

	@Override
	public boolean isLeafNode(){
		return this.isLeafNode;
	}
	
	public void setParent(HtmlNode parent) {
		this.parent = parent;
	}

	public HtmlNode getParent() {
		return parent;
	}

	public void setResidePath(HtmlPath residePath) {
		this.residePath = residePath;
	}

	public String getResidePathID() {
		return residePath.getPathIdent();
	}
	
	public HtmlPath getResidePath() {
		return residePath;
	}

	public void setPrefixPathID(String prefixPath) {
		this.prefixPathID = prefixPath;
	}

	public String getPrefixPathID() {
		return prefixPathID;
	}

	public void setChild(HtmlNode child) {
		this.child = child;
	}

	public HtmlNode getChild() {
		return child;
	}
	
	public Element getWrappedElement(){
		return this.element;
	}
	
	public HtmlNode fomer(int i) {
		HtmlNode node = this;
		for (int index = 0; index < i; index++) {
			node = node.getParent();
			if (node == null)
				break;
		}
		return node;
	}
	
	public String getNodePattern(){

		Map<String, String> attributes = this.listAttributes();
		String attributePattern = this.getAttributePattern(attributes.get("class"), attributes.get("id"), attributes.get("style"));
		StringBuilder patternBuilder = new StringBuilder(this.getTag() + attributePattern);
		
		HtmlNode parentNode = this.getParent();
		while (parentNode != null) {
			Map<String, String> parentAttributes = parentNode.listAttributes();
			String parentAttributePattern = this.getAttributePattern(parentAttributes.get("class"), parentAttributes.get("id"), parentAttributes.get("style"));
			patternBuilder.append("/" + parentNode.getTag() + parentAttributePattern);
			parentNode = parentNode.getParent();
		}

//		StringBuilder patternBuilder = new StringBuilder(this.getTag() + this.getTagCount());
//		
//		WebTagNode parentNode = this.getParent();
//		while (parentNode != null) {
//			patternBuilder.append("/" + parentNode.getTag() + parentNode.getTagCount());
//			parentNode = parentNode.getParent();
//		}
		
		return patternBuilder.toString();
	}
	
	private String getAttributePattern(String classAttri, String idAttri, String styleAttri) {
		
		String attributePattern = null;
		if (classAttri != null && idAttri == null && styleAttri == null) {
			attributePattern = "class:\"" + classAttri + "\"";
		} else if (classAttri == null && idAttri != null && styleAttri == null) {
			attributePattern = "id:\"" + idAttri + "\"";
		} else if (classAttri == null && idAttri == null && styleAttri != null) {
			attributePattern = "style:\"" + styleAttri + "\"";
		} else if (classAttri != null && idAttri != null && styleAttri == null) {
			attributePattern = "class:\"" + classAttri + "\", id:\"" + idAttri + "\"";
		} else if (classAttri != null && idAttri == null && styleAttri != null) {
			attributePattern = "class:\"" + classAttri + "\", style:\"" + styleAttri + "\"";
		} else if (classAttri == null && idAttri != null && styleAttri != null) {
			attributePattern = "id:\"" + idAttri + "\", style:\"" + styleAttri + "\"";
		} else if (classAttri != null && idAttri != null && styleAttri != null) {
			attributePattern = "class:\"" + classAttri + "\", id:\"" + idAttri + "\", style:\"" + styleAttri + "\"";
		}

		attributePattern = attributePattern == null ? "" : "[" + attributePattern + "]";
		return attributePattern;
	}
	
	public void setTagNodeType(NodeType tagNodeType){
		this.nodeType = tagNodeType;
	}

	@Override
	public HtmlNode clone() {
		HtmlNode node = null;
		if (nodeType == NodeType.TextNode) {
			node = new HtmlNode(this.textContent);
		} else {
			node = new HtmlNode(this.element, this.tagCount);
			node.setTagNodeType(this.nodeType);
			node.setContent(this.textContent); 
			node.setLeafNode(this.isLeafNode);
		}
		
		node.setPrefixPathID(this.getPrefixPathID());
		return node;
	}

	public void setLeafNode(boolean isLeafNode) {
		this.isLeafNode = isLeafNode;
	}

	@Override
	public NodeType getNodeType() {
		return nodeType;
	}
}
