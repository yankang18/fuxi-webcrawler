package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.List;

public interface INode {
	
	void addChild(INode child); 
	
	void setParent(INode parent);
	
//	void setSkippable(boolean skippable);
//	
//	boolean isSkippable();
	
	List<INode> getChildren();
	
	INode getParent();

	int getPositionNumber();
	
	String getTagName();
	
	String getPathID();
	
	String getParentPathID();
	
}
