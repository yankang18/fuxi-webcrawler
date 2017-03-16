package umbc.ebiquity.kang.htmldocument.parser.htmltree;

public interface IHTMLTreeOverlay {

	String getUniqueIdentifier();
	
	String getDomainName();

	IHTMLTreeNode getTreeRoot();

	void setRootNote(IHTMLTreeNode root); 

}
