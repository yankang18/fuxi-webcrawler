package umbc.ebiquity.kang.htmldocument.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.IHtmlDocumentNode;
import umbc.ebiquity.kang.textprocessing.util.TextProcessingUtils;
import umbc.ebiquity.kang.websiteparser.impl.HTMLTags;

public class HtmlDocumentNode implements IHtmlDocumentNode{
	
	private HtmlDocumentNode parent;
	private HtmlDocumentNode child;
	private HtmlDocumentPath residePath;
	
	private DocumentNodeType nodeType;
	private String prefixPathID;
	
	private Element element;
	private int tagCount;
	private String textContent;
	private Collection<HtmlDocumentNode> children;
	private Collection<HtmlDocumentNode> sibling;
	private Map<String, String> attributes;
	private boolean isLeafNode = true;
	
	public HtmlDocumentNode(Element node, int tagCount) {
		this.element = node;
		this.tagCount = tagCount;
		this.nodeType = DocumentNodeType.ElementNode;
		this.isLeafNode = false;
		this.textContent = "";
		this.init();
	}
	
	public HtmlDocumentNode(String textNodeContent) {
		this.nodeType = DocumentNodeType.TextNode;
		this.isLeafNode = true;
		this.textContent = textNodeContent;
		this.init();
	}

	/**
	 * You should only call this method on node with element type
	 */
	public void toValueNode() {
		if (this.nodeType == DocumentNodeType.ElementNode) {
			this.nodeType = DocumentNodeType.ValueElementNode;
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
		if (nodeType == DocumentNodeType.ElementNode || nodeType == DocumentNodeType.ValueElementNode) {
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
		if (nodeType == DocumentNodeType.TextNode) {
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

	public Collection<HtmlDocumentNode> listChildren() {
		this.populateChildrenCollection();
		return this.children;
	}

	public Collection<HtmlDocumentNode> listSiblings() {
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
			if(nodeType == DocumentNodeType.TextNode){
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
			this.children = new ArrayList<HtmlDocumentNode>();
			if (nodeType == DocumentNodeType.TextNode || nodeType == DocumentNodeType.ValueElementNode) {
				return;
			}
			for (Element child : this.element.children()) {
				HtmlDocumentNode nodeWrapper = new HtmlDocumentNode(child, 1);
				this.children.add(nodeWrapper);
			}
		}
	}

	private void populateSiblingCollection() {
		if (this.sibling == null) {
			this.sibling = new ArrayList<HtmlDocumentNode>();
			if (nodeType == DocumentNodeType.TextNode) {
				return;
			}
			for (Element child : this.element.siblingElements()) {
				HtmlDocumentNode nodeWrapper = new HtmlDocumentNode(child, 1);
				this.sibling.add(nodeWrapper);
			}
		}
	}

	public boolean isLeafNode(){
		return this.isLeafNode;
	}
	
	public void setParent(HtmlDocumentNode parent) {
		this.parent = parent;
	}

	public HtmlDocumentNode getParent() {
		return parent;
	}

	public void setResidePath(HtmlDocumentPath residePath) {
		this.residePath = residePath;
	}

	public String getResidePathID() {
		return residePath.getPathID();
	}
	
	public HtmlDocumentPath getResidePath() {
		return residePath;
	}

	public void setPrefixPathID(String prefixPath) {
		this.prefixPathID = prefixPath;
	}

	public String getPrefixPathID() {
		return prefixPathID;
	}

	public void setChild(HtmlDocumentNode child) {
		this.child = child;
	}

	public HtmlDocumentNode getChild() {
		return child;
	}
	
	public Element getWrappedElement(){
		return this.element;
	}
	
	public HtmlDocumentNode fomer(int i) {
		HtmlDocumentNode node = this;
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
		
		HtmlDocumentNode parentNode = this.getParent();
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
	
	public void setTagNodeType(DocumentNodeType tagNodeType){
		this.nodeType = tagNodeType;
	}

	@Override
	public HtmlDocumentNode clone() {
		HtmlDocumentNode node = null;
		if (nodeType == DocumentNodeType.TextNode) {
			node = new HtmlDocumentNode(this.textContent);
		} else {
			node = new HtmlDocumentNode(this.element, this.tagCount);
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
	public DocumentNodeType getWebTagNodeType() {
		return nodeType;
	}
}
