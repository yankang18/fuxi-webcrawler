package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

public class HTMLTreeNodeValue {

	public enum ValueType {
		Number, Image, Term, Paragraph /* one or more sentences */
	}

	private String value;
	private ValueType type;
	private String description;

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

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
