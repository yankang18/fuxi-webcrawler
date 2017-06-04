package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeNode;

public class HTMLTreeEntityNode extends AbstractHTMLTreeNode {

	private String content;
	private String normalizedName;
	
	/**
	 * 
	 * @param content
	 * @param element
	 */
	public HTMLTreeEntityNode(Element element, String content){
		super(element);
		this.content = content;
		this.normalizedName = createNormalizedContent(content);
	}
	
	/**
	 * 
	 * @param tagName
	 * @param content
	 */
	public HTMLTreeEntityNode(String tagName, String content){
		super(tagName);
		this.content = content;
		this.normalizedName = createNormalizedContent(content);
	}
	
	private String createNormalizedContent(String content) {
		return content;
	}

	public String getContent(){
		return content;
	}
	
	public String getNormalizedName(){
		return normalizedName;
	}

}
