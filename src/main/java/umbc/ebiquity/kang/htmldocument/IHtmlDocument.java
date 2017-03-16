package umbc.ebiquity.kang.htmldocument;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;

import umbc.ebiquity.kang.htmldocument.impl.HtmlPath;
import umbc.ebiquity.kang.websiteparser.impl.CrawlerUrl;

public interface IHtmlDocument extends IHtmlElement {

	void load() throws IOException;

	Document getDocument();

	// String getDomainName();
	//
	// String getUniqueIdentifier();

	boolean isLoaded();

}
