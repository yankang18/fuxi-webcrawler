package umbc.ebiquity.kang.websiteparser.support.impl;

import java.net.URL;
import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.support.IWebSiteParsedPathsHolder;

public class DefaultWebSiteParsedPathsHolder implements IWebSiteParsedPathsHolder {

	private URL webSiteURL;
	private List<IHtmlDocumentParsedPathsHolder> webPagePathHolders;

	public DefaultWebSiteParsedPathsHolder(URL webSiteURL, List<IHtmlDocumentParsedPathsHolder> webPagePathHolders) {
		this.webSiteURL = webSiteURL;
		this.webPagePathHolders = webPagePathHolders; 
	}

	public List<IHtmlDocumentParsedPathsHolder> getHtmlDocumentParsedPathHolders() {
		return webPagePathHolders;
	}

	public URL getWebSiteURL() {
		return webSiteURL;
	}
}
