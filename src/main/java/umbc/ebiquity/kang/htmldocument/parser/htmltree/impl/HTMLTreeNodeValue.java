package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

public class HTMLTreeNodeValue {

	public enum ValueType {
		Number, Image, Term, Paragraph /* one or more sentences */
	}

	private String value;
	private ValueType type;

	public HTMLTreeNodeValue(String value, ValueType type) {
		this.value = value;
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public ValueType getValueType() {
		return type;
	}

	public void setValueType(ValueType valueType) {
		this.type = valueType;
	}

}
