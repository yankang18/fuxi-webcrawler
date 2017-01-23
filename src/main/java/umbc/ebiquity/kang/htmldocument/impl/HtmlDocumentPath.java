package umbc.ebiquity.kang.htmldocument.impl;

import java.util.Collection;
import java.util.LinkedList;

import umbc.ebiquity.kang.htmldocument.IHtmlDocumentPath;
import umbc.ebiquity.kang.textprocessing.util.TextProcessingUtils;

public class HtmlDocumentPath implements IHtmlDocumentPath {

	private String pathId;
	private LinkedList<HtmlDocumentNode> nodeList;
	private String host;
	private String pathPattern;
	
	public HtmlDocumentPath(){
		pathId = null;
		nodeList = new LinkedList<HtmlDocumentNode>();
	}
	
	public void addNode(HtmlDocumentNode node) {
		HtmlDocumentNode last = null;
		if (nodeList.size() != 0) {
			last = nodeList.getLast();
			last.setChild(node);
		}
		node.setParent(last);
		node.setPrefixPathID(this.getPathID());
		node.setResidePath(this);
		nodeList.add(node);
	}

	public String getPathID() {
		return computePathId();
	}

	public String getPathPattern() {
		return computePathPattern();
	}

	public HtmlDocumentNode getNode(String prefixPathID){
		for(HtmlDocumentNode node : nodeList){
			if(node.getPrefixPathID().equals(prefixPathID)){
				return node;
			}
		}
		return null;
	}
	
	@Override
	public HtmlDocumentPath clone(){
		HtmlDocumentPath newPath = new HtmlDocumentPath();
		newPath.setPathID(this.getPathID());
		newPath.setClonedNodes(this.getClonedNodes());
		return newPath;
	}
	
	private void setPathID(String pathId) {
		this.pathId = pathId;
	}
	
	private void setClonedNodes(Collection<HtmlDocumentNode> nodes) {
		for (HtmlDocumentNode node : nodes) {
			node.setResidePath(this);
			this.nodeList.add(node);
		}
	}
	
	private Collection<HtmlDocumentNode> getClonedNodes() {
		LinkedList<HtmlDocumentNode> newNodes = new LinkedList<HtmlDocumentNode>();
		HtmlDocumentNode parent = null;
		for (HtmlDocumentNode node : this.nodeList) {
			HtmlDocumentNode newNode = node.clone();
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
		for (HtmlDocumentNode node : nodeList) {
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
		for (HtmlDocumentNode node : nodeList) {

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
		for (HtmlDocumentNode node : nodeList) {
			
			if(!TextProcessingUtils.isStringEmpty(node.getFullContent())){
				containsTextContent = true;
			}
			if (containsTextContent) {
				return containsTextContent;
			}
		}
		return containsTextContent;
	}
	
	public HtmlDocumentNode getLastNode() {
		if (nodeList.size() == 0) {
			return null;
		}
		
		return nodeList.getLast();
	}
	
	public HtmlDocumentNode getSecondToLastNode(){
		
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
		HtmlDocumentPath webPagePath = (HtmlDocumentPath) obj;
		return this.getPathPattern().equals(webPagePath.getPathPattern());
	}
}
