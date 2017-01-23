package umbc.ebiquity.kang.htmldocument;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;

import umbc.ebiquity.kang.htmldocument.impl.HtmlDocumentPath;
import umbc.ebiquity.kang.websiteparser.impl.CrawlerUrl;

public interface IHtmlDocument {

	void extractLinks(Set<String> locOfExcludedDoc);

	Map<String, String> getExternalLinks();

	void load() throws IOException;

	Document getDocument();

//	List<IHtmlDocumentPath> listFilteredDocumentPaths();

	String getUniqueIdentifier();

	boolean isLoaded();


}
