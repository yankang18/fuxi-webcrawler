package umbc.ebiquity.kang.htmldocument.parser.htmltree;

import java.util.List;

public interface IHTMLTreeNode {
	
	void addChild(IHTMLTreeNode child); 
	
	void setParent(IHTMLTreeNode parent);
	
	List<IHTMLTreeNode> getChildren();
	
	IHTMLTreeNode getParent();

	int getPositionNumber();
	
	String getTagName();
	
	String getPathID();
	
	String getParentPathID();
	
}
