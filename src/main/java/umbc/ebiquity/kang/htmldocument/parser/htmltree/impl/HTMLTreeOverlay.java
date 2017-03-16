package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;

public class HTMLTreeOverlay implements IHTMLTreeOverlay {

	private String uniqueIdentifier;
	private String domainName;
	private IHTMLTreeNode root;

	public HTMLTreeOverlay(IHTMLTreeNode root, String uniqueIdentifier, String domainName) {
		this.root = root;
		this.uniqueIdentifier = uniqueIdentifier;
		this.domainName = domainName;
	}

	@Override
	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	@Override
	public IHTMLTreeNode getTreeRoot() {
		return root;
	}

	@Override
	public void setRootNote(IHTMLTreeNode root) {
		this.root = root;
	}

	@Override
	public String getDomainName() {
		return domainName;
	}

}
