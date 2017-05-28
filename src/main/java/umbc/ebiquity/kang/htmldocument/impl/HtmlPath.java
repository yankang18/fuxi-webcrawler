package umbc.ebiquity.kang.htmldocument.impl;

import java.util.Collection;
import java.util.LinkedList;

import umbc.ebiquity.kang.htmldocument.IHtmlPath;
import umbc.ebiquity.kang.textprocessing.util.TextProcessingUtils;

public class HtmlPath implements IHtmlPath {

	private String pathId;
	private LinkedList<HtmlNode> nodeList;
	private String host;
	private String pathPattern;
	
	public HtmlPath(){
		pathId = null;
		nodeList = new LinkedList<HtmlNode>();
	}
	
	public void addNode(HtmlNode node) {
		HtmlNode last = null;
		if (nodeList.size() != 0) {
			last = nodeList.getLast();
			last.setChild(node);
		}
		node.setParent(last);
		node.setPrefixPathID(this.getPathIdent());
		node.setResidePath(this);
		nodeList.add(node);
	}

	@Override
	public String getPathIdent() {
		return computePathId();
	}

	public String getPathPattern() {
		return computePathPattern();
	}

	public HtmlNode getNode(String prefixPathID){
		for(HtmlNode node : nodeList){
			if(node.getPrefixPathID().equals(prefixPathID)){
				return node;
			}
		}
		return null;
	}
	
	@Override
	public HtmlPath clone(){
		HtmlPath newPath = new HtmlPath();
		newPath.setPathID(this.getPathIdent());
		newPath.setClonedNodes(this.getClonedNodes());
		return newPath;
	}
	
	private void setPathID(String pathId) {
		this.pathId = pathId;
	}
	
	private void setClonedNodes(Collection<HtmlNode> nodes) {
		for (HtmlNode node : nodes) {
			node.setResidePath(this);
			this.nodeList.add(node);
		}
	}
	
	private Collection<HtmlNode> getClonedNodes() {
		LinkedList<HtmlNode> newNodes = new LinkedList<HtmlNode>();
		HtmlNode parent = null;
		for (HtmlNode node : this.nodeList) {
			HtmlNode newNode = node.clone();
			if (parent != null) {
				parent.setChild(newNode);
			}
			newNode.setParent(parent);
			newNodes.add(newNode);
			parent = newNode;
		}
		return newNodes;
	}
	
	private String computePathId() {
		StringBuilder builder = new StringBuilder();
		for (HtmlNode node : nodeList) {
			if (node.isLeafNode()) {
				builder.append(node.getTag() + node.getTagCount() + "[" + node.getFullContent() + "]");
			} else {
				builder.append(node.getTag() + node.getTagCount() + "/");
			}
		}
		pathId = builder.toString();
		return pathId;
	}
	
	private String computePathPattern() {
		StringBuilder builder = new StringBuilder();
		for (HtmlNode node : nodeList) {

			StringBuilder attributeBuilder = new StringBuilder("[");
			for (String key : node.attributeKeySet()) {
				attributeBuilder.append(key + ":" + node.attributeValue(key) + ",");
			}
			String attributes = node.listAttributes().size() == 0 ? "" : attributeBuilder.substring(0, attributeBuilder.length() - 1) + "]";
			if (node.isLeafNode()) {
				builder.append(node.getTag() + attributes + "[" + node.getFullContent() + "]");
			} else {
				builder.append(node.getTag() + attributes + "/");
			}
		}
		this.pathPattern = builder.toString();
		return pathPattern;
	}

	public boolean containsTextContent() {
		boolean containsTextContent = false;
		for (HtmlNode node : nodeList) {
			
			if(!TextProcessingUtils.isStringEmpty(node.getFullContent())){
				containsTextContent = true;
			}
			if (containsTextContent) {
				return containsTextContent;
			}
		}
		return containsTextContent;
	}
	
	public HtmlNode getLastNode() {
		if (nodeList.size() == 0) {
			return null;
		}
		
		return nodeList.getLast();
	}
	
	public HtmlNode getSecondToLastNode(){
		
		if (nodeList.size() == 1) {
			return null;
		}
		
		return nodeList.get(nodeList.size() - 2);
	}
	
	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return host;
	}
	
	@Override
	public int hashCode(){
		return this.getPathPattern().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		HtmlPath webPagePath = (HtmlPath) obj;
		return this.getPathPattern().equals(webPagePath.getPathPattern());
	}
}
