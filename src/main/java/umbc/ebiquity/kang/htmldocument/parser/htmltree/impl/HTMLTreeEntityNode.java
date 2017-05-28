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
	public HTMLTreeEntityNode(String content, Element element){
		super(element);
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
