package umbc.ebiquity.kang.htmldocument;

import org.jsoup.nodes.Element;

public interface IHtmlDocumentNode {
	
	public enum DocumentNodeType {
		TextNode, ElementNode, ValueElementNode
	}
	
	DocumentNodeType getWebTagNodeType(); 
	
	String getTag();

	String getFullContent();

	String getNodePattern();

	IHtmlDocumentNode getParent();

	String getPrefixPathID();

	Element getWrappedElement();  
 
}
