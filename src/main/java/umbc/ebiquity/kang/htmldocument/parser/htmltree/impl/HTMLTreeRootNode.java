package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import java.util.ArrayList;
import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;

public class HTMLTreeRootNode implements IHTMLTreeNode {
	private List<IHTMLTreeNode> children;
	private String pathID; 
	private String parentPathID; 

	public HTMLTreeRootNode() {
		children = new ArrayList<IHTMLTreeNode>();
	}

	@Override
	public List<IHTMLTreeNode> getChildren() {
		return children;
	}

	@Override
	public void addChild(IHTMLTreeNode child) {
		this.children.add(child);
	}

	@Override
	public int getPositionNumber() {
		return 1;
	}

	@Override
	public String getTagName() {
		return "Root";
	}
	
	public void setPathID(String pathID){
		this.pathID = pathID;
	}
	
	public void setParentPathID(String parentPathID){
		this.parentPathID = parentPathID;
	}

	@Override
	public String getPathID() {
		return pathID;
	}

	@Override
	public void setParent(IHTMLTreeNode parent) {
		// Does nothing
	}

	@Override
	public IHTMLTreeNode getParent() {
		return null;
	}

	@Override
	public String getParentPathID() {
		return parentPathID;
	}

}
