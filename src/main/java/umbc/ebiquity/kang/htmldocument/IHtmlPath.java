package umbc.ebiquity.kang.htmldocument;

public interface IHtmlPath {

	String getPathIdent();

	IHtmlNode getLastNode();

	String getHost();

	IHtmlNode getNode(String prefixPathID);

	boolean containsTextContent();     

}
