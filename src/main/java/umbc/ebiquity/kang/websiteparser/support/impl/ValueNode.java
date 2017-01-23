package umbc.ebiquity.kang.websiteparser.support.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

public class ValueNode extends AbstractNode {

	private List<Value> values;
	private Value mainValue;

	public ValueNode(Value value, Element element) {
		super(element);
		mainValue = value;
		values = new ArrayList<Value>();
	}
	
	public ValueNode(Value value, String tagName) {
		super(tagName);
		mainValue = value;
		values = new ArrayList<Value>();
	}
	
	public void addValue(Value value) {
		values.add(value);
	}
	
	public void addValues(List<Value> values) {
		values.addAll(values);
	}

	public List<Value> getValues() { 
		return values;
	}

	public boolean hasValue() {
		return !values.isEmpty();
	}
	
	public Value getMainValue(){
		return mainValue;
	}
}
