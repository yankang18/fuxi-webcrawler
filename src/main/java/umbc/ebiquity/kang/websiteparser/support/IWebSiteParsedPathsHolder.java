package umbc.ebiquity.kang.websiteparser.support;

import java.net.URL;
import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;

public interface IWebSiteParsedPathsHolder {

	List<IHtmlDocumentParsedPathsHolder> getHtmlDocumentParsedPathHolders();

	URL getWebSiteURL();
}
