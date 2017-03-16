package umbc.ebiquity.kang.htmldocument;

import org.jsoup.nodes.Element;

public interface IHtmlElement {

	String getDomainName();

	String getUniqueIdentifier();

	Element getBody();
}
