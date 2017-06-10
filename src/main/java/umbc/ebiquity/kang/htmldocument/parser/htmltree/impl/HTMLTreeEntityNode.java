package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import org.jsoup.nodes.Element;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeContentNode;

public class HTMLTreeEntityNode extends AbstractHTMLTreeContentNode {

	/**
	 * Constructor.
	 * 
	 * @param content
	 * @param element
	 */
	public HTMLTreeEntityNode(Element element, String content) {
		super(element, content);
	}

	/**
	 * Constructor.
	 * 
	 * @param tagName
	 * @param content
	 */
	public HTMLTreeEntityNode(String tagName, String content) {
		super(tagName, content);
	}
}
