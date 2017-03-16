package umbc.ebiquity.kang.websiteparser.support.impl;

import java.net.URL;
import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.IHtmlParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.support.IWebSiteParsedPathsHolder;

public class DefaultWebSiteParsedPathsHolder implements IWebSiteParsedPathsHolder {

	private URL webSiteURL;
	private List<IHtmlParsedPathsHolder> webPagePathHolders;

	public DefaultWebSiteParsedPathsHolder(URL webSiteURL, List<IHtmlParsedPathsHolder> webPagePathHolders) {
		this.webSiteURL = webSiteURL;
		this.webPagePathHolders = webPagePathHolders; 
	}

    /*
     * (non-Javadoc)
     * @see umbc.ebiquity.kang.entityframework.support.IWebSitePathHolder#getWebPagePathHolders()
     */
	public List<IHtmlParsedPathsHolder> getHtmlDocumentParsedPathHolders() {
		return webPagePathHolders;
	}

	/*
	 * (non-Javadoc)
	 * @see umbc.ebiquity.kang.entityframework.support.IWebSitePathHolder#getWebSiteURL()
	 */
	public URL getWebSiteURL() {
		return webSiteURL;
	}
}
