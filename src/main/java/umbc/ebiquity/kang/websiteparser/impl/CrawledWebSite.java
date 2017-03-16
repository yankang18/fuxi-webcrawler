package umbc.ebiquity.kang.websiteparser.impl;

import java.net.URL;
import java.util.List;

import umbc.ebiquity.kang.websiteparser.ICrawledWebSite;
import umbc.ebiquity.kang.websiteparser.IWebPageDocument;

public class CrawledWebSite implements ICrawledWebSite {

	private List<IWebPageDocument> webPages;
	private URL webSiteURL;

	CrawledWebSite(List<IWebPageDocument> webPages, URL webSiteURL) {
		this.webPages = webPages;
		this.webSiteURL = webSiteURL;
	}

	@Override
	public List<IWebPageDocument> getWebPages() {
		return webPages;
	}

	@Override
	public URL getWebSiteURL() {
		return webSiteURL;
	}

}
