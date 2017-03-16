package umbc.ebiquity.kang.htmldocument;

public interface IHtmlPath {

	String getPathID();

	IHtmlNode getLastNode();

	String getHost();

	IHtmlNode getNode(String prefixPathID);

	boolean containsTextContent();     

}
