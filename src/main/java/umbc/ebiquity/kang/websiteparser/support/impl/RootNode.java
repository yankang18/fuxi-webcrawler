package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.ArrayList;
import java.util.List;

public class RootNode implements INode {
	private List<INode> children;
	private String pathID; 
	private String parentPathID; 

	public RootNode() {
		children = new ArrayList<INode>();
	}

	@Override
	public List<INode> getChildren() {
		return children;
	}

	@Override
	public void addChild(INode child) {
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
	public void setParent(INode parent) {
		// Does nothing
	}

	@Override
	public INode getParent() {
		return null;
	}

	@Override
	public String getParentPathID() {
		return parentPathID;
	}

}
