package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeNode;

public class HTMLTreeBlankNode extends AbstractHTMLTreeNode {
	
	private boolean skippable;

	public HTMLTreeBlankNode(Element element) {
		super(element);
		skippable = true;
	}

	public HTMLTreeBlankNode(String tagName) {
		super(tagName);
		skippable = true;
	}

	public void setSkippable(boolean isSkippable) {
		skippable = isSkippable;
	}

	public boolean isSkippable() {
		return skippable;
	}
}
