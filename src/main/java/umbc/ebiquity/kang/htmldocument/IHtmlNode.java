package umbc.ebiquity.kang.htmldocument;

import org.jsoup.nodes.Element;

public interface IHtmlNode {
	
	public enum NodeType {
		TextNode, ElementNode, ValueElementNode
	}
	
	NodeType getNodeType(); 
	
	String getTag();

	String getFullContent();

	String getNodePattern();

	IHtmlNode getParent();

	String getPrefixPathID();

	Element getWrappedElement();

	boolean isLeafNode();
 
}
