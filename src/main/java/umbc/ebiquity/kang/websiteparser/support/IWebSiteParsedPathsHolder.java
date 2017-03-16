package umbc.ebiquity.kang.websiteparser.support;

import java.net.URL;
import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.IHtmlParsedPathsHolder;

public interface IWebSiteParsedPathsHolder {

	List<IHtmlParsedPathsHolder> getHtmlDocumentParsedPathHolders();

	URL getWebSiteURL();
}
