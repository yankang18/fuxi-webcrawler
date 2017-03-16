package umbc.ebiquity.kang.htmldocument.parser.htmltree;

import umbc.ebiquity.kang.htmldocument.IHtmlElement;

public interface ICustomizedHTMLNodeProcessor {

	boolean isMatched(IHtmlElement htmlElement); 

	IHTMLTreeNode process(IHtmlElement htmlElement);

}
