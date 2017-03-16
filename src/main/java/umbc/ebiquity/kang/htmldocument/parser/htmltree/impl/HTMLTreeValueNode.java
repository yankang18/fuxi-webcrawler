package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.AbstractHTMLTreeNode;

public class HTMLTreeValueNode extends AbstractHTMLTreeNode {

	private List<HTMLTreeNodeValue> values;
	private HTMLTreeNodeValue mainValue;

	public HTMLTreeValueNode(HTMLTreeNodeValue value, Element element) {
		super(element);
		mainValue = value;
		values = new ArrayList<HTMLTreeNodeValue>();
	}
	
	public HTMLTreeValueNode(HTMLTreeNodeValue value, String tagName) {
		super(tagName);
		mainValue = value;
		values = new ArrayList<HTMLTreeNodeValue>();
	}
	
	public void addValue(HTMLTreeNodeValue value) {
		values.add(value);
	}
	
	public void addValues(List<HTMLTreeNodeValue> values) {
		values.addAll(values);
	}

	public List<HTMLTreeNodeValue> getValues() { 
		return values;
	}

	public boolean hasValue() {
		return !values.isEmpty();
	}
	
	public HTMLTreeNodeValue getMainValue(){
		return mainValue;
	}
}
