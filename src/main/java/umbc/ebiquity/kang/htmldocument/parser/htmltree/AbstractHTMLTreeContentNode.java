package umbc.ebiquity.kang.htmldocument.parser.htmltree;

import org.jsoup.nodes.Element;

public abstract class AbstractHTMLTreeContentNode extends AbstractHTMLTreeNode {
	
	private String content;
	private String normalizedContent;

	/**
	 * Constructor.
	 * 
	 * @param element
	 * @param content
	 */
	protected AbstractHTMLTreeContentNode(Element element, String content) {
		super(element);
		this.content = content;
		this.normalizedContent = createNormalizedContent(content);
	}

	/**
	 * Constructor.
	 * 
	 * @param tagName
	 * @param content
	 */
	protected AbstractHTMLTreeContentNode(String tagName, String content) {
		super(tagName);
		this.content = content;
		this.normalizedContent = createNormalizedContent(content);
	}

	private String createNormalizedContent(String content) {
		return content;
	}

	public String getContent() {
		return content;
	}

	public String getNormalizedContent() {
		return normalizedContent;
	}
}
