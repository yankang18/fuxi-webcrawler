package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import umbc.ebiquity.kang.htmldocument.IHtmlDocument;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlDocumentParsedPathsHolder;
import umbc.ebiquity.kang.websiteparser.ICrawledWebSite;
import umbc.ebiquity.kang.websiteparser.support.IPathFocusedWebSiteParser;
import umbc.ebiquity.kang.websiteparser.support.IWebSiteParsedPathsHolder;

public class DefaultPathFocusedWebSiteParser implements IPathFocusedWebSiteParser {

	private ICrawledWebSite website;

	DefaultPathFocusedWebSiteParser(ICrawledWebSite website) {
		this.website = website;
	}

	public IWebSiteParsedPathsHolder parse() {
		List<IHtmlDocumentParsedPathsHolder> webPagePathHolders = new ArrayList<IHtmlDocumentParsedPathsHolder>();
		for (IHtmlDocument webPage : website.getWebPages()) {
			// TODO: may user static method to parse web page for saving memory
			DefaultWebPagePathsParser webPagePathsImpl = new DefaultWebPagePathsParser(webPage);
			IHtmlDocumentParsedPathsHolder webPagePathHolder = webPagePathsImpl.parse();
			webPagePathHolders.add(webPagePathHolder);
		}
		return new DefaultWebSiteParsedPathsHolder(website.getWebSiteURL(), webPagePathHolders);
	}

}
