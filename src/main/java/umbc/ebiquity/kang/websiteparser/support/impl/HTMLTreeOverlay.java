package umbc.ebiquity.kang.websiteparser.support.impl;

import umbc.ebiquity.kang.websiteparser.support.IHTMLTreeOverlay;

public class HTMLTreeOverlay implements IHTMLTreeOverlay {

	private String uniqueIdentifier;
	private INode root;

	public HTMLTreeOverlay(INode root, String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
		this.root = root;
	}

	@Override
	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	@Override
	public INode getTreeRoot() {
		return root;
	}

	@Override
	public void setRootNote(INode root) {
		this.root = root;
	}

}
