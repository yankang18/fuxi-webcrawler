package umbc.ebiquity.kang.websiteparser;

import umbc.ebiquity.kang.htmldocument.IHtmlDocument;

public interface IWebPageDocument extends IHtmlDocument {

	String getHostName();

	String getBaseURL();
	
	String getWebPageTopic();

	void setHostName(String hostName);
	
	void setWebPageTopic(String webPageMainTopic); 

	void addDecendant(IHtmlDocument webPage);

	void addPredecessor(IHtmlDocument top);
}
