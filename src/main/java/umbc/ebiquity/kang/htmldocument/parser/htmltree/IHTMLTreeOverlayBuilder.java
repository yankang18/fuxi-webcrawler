package umbc.ebiquity.kang.htmldocument.parser.htmltree;

import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;

public interface IHTMLTreeOverlayBuilder {

	IHTMLTreeOverlay build(IHtmlDocumentParsedPathsHolder htmlParsedPathsHolder);
}
