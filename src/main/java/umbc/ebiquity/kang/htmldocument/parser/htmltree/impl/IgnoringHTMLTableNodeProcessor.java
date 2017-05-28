package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import umbc.ebiquity.kang.htmldocument.IHtmlElement;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.ICustomizedHTMLNodeProcessor;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;

public class IgnoringHTMLTableNodeProcessor implements ICustomizedHTMLNodeProcessor {

	@Override
	public boolean isMatched(IHtmlElement htmlElement) {
		return "table".equalsIgnoreCase(htmlElement.getBody().tagName());
	}

	@Override
	public IHTMLTreeNode process(IHtmlElement htmlElement) {
		return null;
	}

}
