package umbc.ebiquity.kang.websiteparser.support;

import umbc.ebiquity.kang.websiteparser.support.impl.INode;

public interface IHTMLTreeOverlay {

	String getUniqueIdentifier();

	INode getTreeRoot();

	void setRootNote(INode root); 

}
