package umbc.ebiquity.kang.websiteparser.support.impl;

public class Value {

	public enum ValueType {
		Number, Image, Term, Paragraph /* a or more sentences */
	}

	private String value;
	private ValueType type;

	public Value(String value, ValueType type) {
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
