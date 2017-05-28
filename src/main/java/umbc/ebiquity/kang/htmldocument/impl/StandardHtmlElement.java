package umbc.ebiquity.kang.htmldocument.impl;

import java.util.UUID;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.IHtmlElement;

public class StandardHtmlElement implements IHtmlElement {

	private Element element;
	private String uniqueIdentifier;
	private String domainName;

	public StandardHtmlElement(Element element, String uniqueIdentifier, String domainName) {
		this.element = element;
		this.uniqueIdentifier = uniqueIdentifier;
		this.domainName = domainName;
	}

	public static StandardHtmlElement createDefaultStandardHtmlElement(Element element) {
		return new StandardHtmlElement(element, UUID.randomUUID().toString(), "");
	}

	@Override
	public String getDomainName() {
		return domainName;
	}

	@Override
	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	@Override
	public Element getBody() {
		return element;
	}

}
