package umbc.ebiquity.kang.htmldocument.parser.htmltree;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

public class AbstractHTMLTreeNode implements IHTMLTreeNode {

	private Element elem;
	private List<IHTMLTreeNode> children;
	private IHTMLTreeNode parent;
	private int positionNumber;
	private String pathID;
	private String parentPathID;
	private String tagName;

	/**
	 * 
	 * @param element
	 */
	protected AbstractHTMLTreeNode(Element element) {
		children = new ArrayList<IHTMLTreeNode>();
		elem = element;
	}

	/**
	 * 
	 * @param tagName
	 */
	protected AbstractHTMLTreeNode(String tagName) {
		children = new ArrayList<IHTMLTreeNode>();
		this.tagName = tagName;
	}

	@Override
	public List<IHTMLTreeNode> getChildren() {
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

	/**
	 * Get the Element object that is encapsulated by this HTMLTreeNode object.
	 * 
	 * @return Element object
	 */
	public Element getWrappedElement() {
		return elem;
	}

	@Override
	public String getPathID() {
		return pathID;
	}

	@Override
	public IHTMLTreeNode getParent() {
		return parent;
	}

	@Override
	public String getParentPathID() {
		return parentPathID;
	}

	public void addChild(IHTMLTreeNode child) {
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
	public void setParent(IHTMLTreeNode parent) {
		this.parent = parent;
	}

}
