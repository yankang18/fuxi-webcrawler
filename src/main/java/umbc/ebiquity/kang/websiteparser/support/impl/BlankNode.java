package umbc.ebiquity.kang.websiteparser.support.impl;

import org.jsoup.nodes.Element;

public class BlankNode extends AbstractNode {
	
	private boolean skippable;
	
	public BlankNode(Element element){
		super(element);
		skippable = true;
	}

	public void setSkippable(boolean isSkippable) {
		skippable = isSkippable;
	}

	public boolean isSkippable() {
		return skippable;
	}

}
