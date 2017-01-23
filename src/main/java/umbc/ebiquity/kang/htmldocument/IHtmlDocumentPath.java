package umbc.ebiquity.kang.htmldocument;

public interface IHtmlDocumentPath {

	String getPathID();

	IHtmlDocumentNode getLastNode();

	String getHost();

	IHtmlDocumentNode getNode(String prefixPathID);

	boolean containsTextContent();     

}
