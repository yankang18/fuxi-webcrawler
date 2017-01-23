package umbc.ebiquity.kang.websiteparser.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.htmldocument.AbstractHTMLDocument;
import umbc.ebiquity.kang.htmldocument.IHtmlDocument;
import umbc.ebiquity.kang.htmldocument.IHtmlDocumentPath;
import umbc.ebiquity.kang.htmldocument.impl.HtmlDocumentPath;
import umbc.ebiquity.kang.websiteparser.IWebPageDocument;

public class WebPageImpl extends AbstractHTMLDocument implements IWebPageDocument {

	private String baseURL;
	private String hostName;
	private CrawlerUrl pageURL;
	
	private List<IHtmlDocument> predecessorList;
	private List<IHtmlDocument> descendantList;
	private UrlValidator urlValidator;
	private String webPageMainTopic;

	WebPageImpl(CrawlerUrl pageURL) throws IOException {
		super();
		this.pageURL = pageURL;
		this.predecessorList = new ArrayList<IHtmlDocument>();
		this.descendantList = new ArrayList<IHtmlDocument>();
		String[] schemes = { "http", "https" };
		this.urlValidator = new UrlValidator(schemes);
	}

	protected Document loadDocument(String location) throws IOException {
		return Jsoup.connect(location).get();
	}
	
	protected String getDocLocation() {
		return this.pageURL.getUrlString();
	}
	
	protected boolean validateLocation(String URL) {
		if (urlValidator.isValid(URL)) {
			return true;
		} else {
			return false;
		}
	}
	
	protected void postProcess(Document webPageDoc){
		this.baseURL = webPageDoc.baseUri();
		this.hostName = this.extractHostName(baseURL);
	}

	private String extractHostName(String baseURL) {
		String[] tokens = baseURL.split("/");
		return tokens[2];
	}

	@Override
	public String getHostName() {
		return this.hostName;
	}

	@Override
	public void setHostName(String hostName){
		this.hostName = hostName;
	}
	
	@Override
	public void addPredecessor(IHtmlDocument pageNode) {
		this.predecessorList.add(pageNode);
	}
	
	@Override
	public void addDecendant(IHtmlDocument pageNode){
		this.descendantList.add(pageNode);
	}

	public CrawlerUrl getPageURL() {
		return pageURL;
	}
	
	@Override
	public String getUniqueIdentifier(){
		return pageURL.getUrlString();
	}

//	/**
//	 * 
//	 * @return
//	 */
//	public List<IHtmlDocumentPath> listFilteredDocumentPaths() {
//		List<IHtmlDocumentPath> paths = new ArrayList<IHtmlDocumentPath>();
//		for (HtmlDocumentPath path : pathList) {
//			if (path.containsTextContent() || path.getLastNode().getTag().equals("img")) {
//				paths.add(path);
//			}
//		}
//		return paths;
//	}
	
	/**
	 * extract out-going links from the WebPage instance
	 * @param visitedLinkSet the Set of visited links to web pages
	 */
	public void extractLinks(Set<String> visitedLinkSet) {
//		System.out.println("base url " + hostName);
		Elements links = getDocument().select("a[href]");
//		print("Links (%d)", links.size());
		for (Element link : links) {
			String href = link.attr("abs:href").trim();
			
			if(visitedLinkSet.contains(href)){
				continue;
			}
			
//			System.out.println("---- " + href);
			if(validateLink(href)){
//				System.out.println("---- " + true);
				String text = link.text();
				addExternalLink(href, text);
			} else {
//				System.out.println("---- " + false);
			}
		}
	}
	
	private boolean validateLink(String href) {
		if (this.validateLocation(href) && this.isWebPage(href) && this.withinHostDomain(href) && this.isUsefulWebPage(href)) {
			return true;
		}
		return false;
	}
	
	private boolean isUsefulWebPage(String href){

		List<String> list = new ArrayList<String>();
		String[] tokens = href.split("/");
		for (String token : tokens) {
			for(String token2 : token.split("_|-|\\.") )
				list.add(token2);
		} 
		Set<String> XXX = new HashSet<String>();
		XXX.add("blog");
		XXX.add("blogs");
		XXX.add("faq");
		XXX.add("faqs");
		XXX.add("carreer");
		XXX.add("carreers");
		XXX.add("portfolios");
		XXX.add("email");
		XXX.add("Email");
		XXX.add("request");
		
		for (String token : list) {
//			System.out.println("Token: " + token);
			if (XXX.contains(token)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isWebPage(String href){
		if(href.endsWith(".pdf") || href.endsWith(".jpg")){
			return false;
		}
		return true;
	}
	
	private boolean withinHostDomain(String href){
		String trimedHref = href.replace("http://", "").replace("http://", "");
		return trimedHref.startsWith(hostName);
	}
	
	public String getBaseURL(){
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

}
