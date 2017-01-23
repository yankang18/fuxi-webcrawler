package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

public class AbstractNode implements INode {

	private Element elem;
	private List<INode> children;
	private INode parent;
	private int positionNumber;
	private String pathID;
	private String parentPathID;
	private String tagName;

	public AbstractNode(Element element) {
		children = new ArrayList<INode>();
		elem = element;
	}

	public AbstractNode(String tagName) {
		children = new ArrayList<INode>();
		this.tagName = tagName;
	}

	@Override
	public List<INode> getChildren() {
		return children;
	}

	@Override
	public int getPositionNumber() {
		return positionNumber;
	}

	@Override
	public String getTagName() {
		return elem == null ? tagName : elem.tagName();
	}
	
	public Element getWrappedElement() {
		return elem;
	}

	@Override
	public String getPathID() {
		return pathID;
	}

	@Override
	public INode getParent() {
		return parent;
	}

	@Override
	public String getParentPathID() {
		return parentPathID;
	}

	public void addChild(INode child) {
		child.setParent(this);
		this.children.add(child);
	}

	public void setPathID(String pathID) {
		this.pathID = pathID;
	}

	public void setParentPathID(String parentPathID) {
		this.parentPathID = parentPathID;
	}

	public void setPositionNumber(int number) {
		this.positionNumber = number;
	}

	@Override
	public void setParent(INode parent) {
		this.parent = parent;
	}

}
