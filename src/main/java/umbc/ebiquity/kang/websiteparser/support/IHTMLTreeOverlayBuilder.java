package umbc.ebiquity.kang.websiteparser.support;

import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.support.impl.INode;

public interface IHTMLTreeOverlayBuilder {

	IHTMLTreeOverlay build(IHtmlDocumentParsedPathsHolder webPagePathHolder);
}
