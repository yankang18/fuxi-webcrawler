package umbc.ebiquity.kang.websiteparser.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import umbc.ebiquity.kang.htmldocument.IHtmlDocument;
import umbc.ebiquity.kang.websiteparser.ICrawledWebSite;
import umbc.ebiquity.kang.websiteparser.ICrawler;
import umbc.ebiquity.kang.websiteparser.IWebPageDocument;
import umbc.ebiquity.kang.websiteparser.support.ITargetLinksExtractionStrategy;
import umbc.ebiquity.kang.websiteparser.support.impl.StandardTargetLinksExtractor;

public class WebSiteCrawler implements ICrawler {

	private final static int MAX_VISIT_PAGE = 1000;
	private Queue<IWebPageDocument> webPageQueue;
	private HashMap<String, IWebPageDocument> visitedPageDir;
	private HashSet<String> visitedPage;

	private String homePageURLString;
	private String domainName;
	private URL webSiteURL;
	private IWebPageDocument homePage;
	private ICrawledWebSite crawledWebSite;
	private ITargetLinksExtractionStrategy targetLinksExtractor;
	private int maxNumberPagesToVisit;
	private boolean isCrawled;

	WebSiteCrawler(URL siteURL, int maxNumberPagesToVisit) throws IOException {
		this.webSiteURL = siteURL;
		this.homePageURLString = siteURL.toString().trim();
		this.domainName = siteURL.getHost();
		this.homePage = new WebPageImpl(new CrawlerUrl(homePageURLString, 0));
		this.webPageQueue = new LinkedList<IWebPageDocument>();
		this.visitedPageDir = new HashMap<String, IWebPageDocument>();
		this.visitedPage = new HashSet<String>();
		this.isCrawled = false;
		this.maxNumberPagesToVisit = maxNumberPagesToVisit;
		this.targetLinksExtractor = new StandardTargetLinksExtractor();
	}

	WebSiteCrawler(URL start) throws IOException {
		this(start, MAX_VISIT_PAGE);
	}

	@Override
	public ICrawledWebSite crawl() throws IOException {
		System.out.println("Crawling Web Site ...");

		if (isCrawled)
			return crawledWebSite;

		webPageQueue.clear();
		homePage.load();
		// homePage.extractLinks(visitedPage);
		webPageQueue.add(homePage);

//		String domainName = homePage.getDomainName();

		// BFS crawling
		while (this.continueCrawling()) {
			IWebPageDocument top = webPageQueue.remove();
			// Map<String, String> links = top.getExternalLinks();
			Set<TargetLink> links = top.extractLinks(targetLinksExtractor, visitedPage);
			for (TargetLink link : links) {
				String webPageUrl = link.getUrl();
				if (this.isValidated(webPageUrl)) {
					IWebPageDocument webPage;
					String topic = link.getTopic();
					try {
						webPage = new WebPageImpl(new CrawlerUrl(webPageUrl, 0));
						webPage.setDomainName(domainName);
						webPage.setWebPageTopic(topic);
						webPage.load();

						if (relevantContent(webPage)) {
							System.out.println("@ Now crawling [" + webPage.getUniqueIdentifier()
									+ "] with main topic [" + topic + "]");

							webPage.addPredecessor(top);
							top.addDecendant(webPage);
							webPageQueue.add(webPage);
						}

						recordVisitedWebPage(webPage);

					} catch (IOException e) {
						continue;
					}
				}
			}
		}
		isCrawled = true;
		crawledWebSite = createCrawledWebSite();
		System.out.println(visitedPageDir.size() + " have been crawled");
		return crawledWebSite;
	}

	@Override
	public ICrawledWebSite getCrawledWebSite() throws IOException {
		if (!this.isCrawled) {
			crawl();
		}
		return crawledWebSite;
	}

	protected boolean relevantContent(IWebPageDocument webPage) {
		return true;
	}

	private ICrawledWebSite createCrawledWebSite() {
		return new CrawledWebSite(new ArrayList<IWebPageDocument>(this.visitedPageDir.values()), this.webSiteURL);
	}

	private void recordVisitedWebPage(IWebPageDocument webPage) {
		visitedPageDir.put(webPage.getUniqueIdentifier(), webPage);
		visitedPage.add(webPage.getUniqueIdentifier());
		// webPage.extractLinks(visitedPage);
	}

	/**
	 * if no web page exists or the max number of web pages to be visited was
	 * reached, return false indicating that web crawling should be continued.
	 * Otherwise, return true.
	 * 
	 * @return true if continue crawling the web site. Otherwise, return false
	 */
	private boolean continueCrawling() {
		return !webPageQueue.isEmpty() && getNumberOfPagesVisited() < maxNumberPagesToVisit;
	}

	private boolean isValidated(String webPageUrl) {
		if (!visitedPage.contains(webPageUrl)) {
			return true;
		} else {
			return false;
		}
	}

	private int getNumberOfPagesVisited() {
		return this.visitedPageDir.size();
	}

}
