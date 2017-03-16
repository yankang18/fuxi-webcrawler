package umbc.ebiquity.kang.websiteparser;

import java.util.Set;

import umbc.ebiquity.kang.htmldocument.IHtmlDocument;
import umbc.ebiquity.kang.websiteparser.impl.TargetLink;
import umbc.ebiquity.kang.websiteparser.support.ITargetLinksExtractionStrategy;

public interface IWebPageDocument extends IHtmlDocument {

	Set<TargetLink> extractLinks(ITargetLinksExtractionStrategy targetLinksExtractor, Set<String> locOfExcludedDoc);

	Set<TargetLink> getExternalLinks();
	
	String getDomainName();

	String getBaseURL();
	
	String getWebPageTopic();

	void setDomainName(String hostName);
	
	void setWebPageTopic(String webPageMainTopic); 

	void addDecendant(IHtmlDocument webPage);

	void addPredecessor(IHtmlDocument top);
}
