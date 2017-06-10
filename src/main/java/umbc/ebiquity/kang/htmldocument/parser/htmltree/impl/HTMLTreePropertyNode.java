package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeContentNode;

public class HTMLTreePropertyNode extends AbstractHTMLTreeContentNode {


	/**
	 * Constructor.
	 * 
	 * @param element
	 * @param content
	 */
	public HTMLTreePropertyNode(Element element, String content) {
		super(element, content);
	}

	/**
	 * Constructor.
	 * 
	 * @param tagName
	 * @param content
	 */
	public HTMLTreePropertyNode(String tagName, String content) {
		super(tagName, content);
	}
}
