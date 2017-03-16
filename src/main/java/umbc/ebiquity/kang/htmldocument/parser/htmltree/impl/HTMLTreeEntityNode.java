package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeNode;

public class HTMLTreeEntityNode extends AbstractHTMLTreeNode {

	private String name;
	private String normalizedName;
	public HTMLTreeEntityNode(String name, Element element){
		super(element);
		this.name = name;
		this.normalizedName = createNormalizedName(name);
	}
	
	private String createNormalizedName(String name) {
		return name;
	}

	public String getName(){
		return name;
	}
	
	public String getNormalizedName(){
		return normalizedName;
	}

}
