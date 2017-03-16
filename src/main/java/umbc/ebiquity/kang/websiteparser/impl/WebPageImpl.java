package umbc.ebiquity.kang.websiteparser.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import umbc.ebiquity.kang.htmldocument.AbstractHTMLDocument;
import umbc.ebiquity.kang.htmldocument.IHtmlDocument;
import umbc.ebiquity.kang.websiteparser.IWebPageDocument;
import umbc.ebiquity.kang.websiteparser.support.ITargetLinksExtractionStrategy;

public class WebPageImpl extends AbstractHTMLDocument implements IWebPageDocument {

	private String baseURL;
	private String hostName;
	private CrawlerUrl pageURL;

	private List<IHtmlDocument> predecessorList;
	private List<IHtmlDocument> descendantList;
	private UrlValidator urlValidator;
	private String webPageMainTopic;
	private Set<TargetLink> externalLinks;

	WebPageImpl(CrawlerUrl pageURL) throws IOException {
		super();
		this.pageURL = pageURL;
		this.predecessorList = new ArrayList<IHtmlDocument>();
		this.descendantList = new ArrayList<IHtmlDocument>();
		String[] schemes = { "http", "https" };
		this.urlValidator = new UrlValidator(schemes);
		this.externalLinks = new LinkedHashSet<TargetLink>();
	}

	protected Document loadDocument(String location) throws IOException {
		return Jsoup.connect(location).get();
	}

	protected String getDocLocation() {
		return this.pageURL.getUrlString();
	}

	protected boolean isValid(String URL) {
		if (urlValidator.isValid(URL)) {
			return true;
		} else {
			return false;
		}
	}

	protected void postProcess(Document webPageDoc) {
		this.baseURL = webPageDoc.baseUri();
		this.hostName = this.extractHostName(baseURL);
	}

	private String extractHostName(String baseURL) {
		String[] tokens = baseURL.split("/");
		return tokens[2];
	}

	@Override
	public String getDomainName() {
		return this.hostName;
	}

	@Override
	public void setDomainName(String hostName) {
		this.hostName = hostName;
	}

	@Override
	public void addPredecessor(IHtmlDocument pageNode) {
		this.predecessorList.add(pageNode);
	}

	@Override
	public void addDecendant(IHtmlDocument pageNode) {
		this.descendantList.add(pageNode);
	}

	public CrawlerUrl getPageURL() {
		return pageURL;
	}

	@Override
	public String getUniqueIdentifier() {
		return pageURL.getUrlString();
	}

	/**
	 * extract out-going links from the WebPage instance
	 * 
	 * @param visitedLinkSet
	 *            the Set of visited links to web pages
	 */
	public Set<TargetLink> extractLinks(ITargetLinksExtractionStrategy targetLinksExtractor, Set<String> visitedLinkSet) {
		getExternalLinks().clear();
		// System.out.println("base url " + hostName);
		Set<TargetLink> webPageLinks = targetLinksExtractor.extract(getDocument());
		// print("Links (%d)", links.size());
		for (TargetLink link : webPageLinks) {
			if (!isValid(link.getUrl()) || visitedLinkSet.contains(link.getUrl())) {
				continue;
			}
			addExternalLink(link);
		}
		return getExternalLinks();
	}

	@Override
	public Set<TargetLink> getExternalLinks() {
		return this.externalLinks;
	}

	public String getBaseURL() {
		return this.baseURL;
	}

	@Override
	public void setWebPageTopic(String webPageMainTopic) {
		this.webPageMainTopic = webPageMainTopic;
	}

	@Override
	public String getWebPageTopic() {
		return webPageMainTopic;
	}

	private void addExternalLink(TargetLink link) {
		externalLinks.add(link);
	}

}
