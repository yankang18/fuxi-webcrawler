package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.ValueTypeInfo;

public class HTMLTreeNodeValue {

	private String content;
	private ValueTypeInfo type;
	private String description;

	public HTMLTreeNodeValue(String content, ValueTypeInfo valueTypeInfo) {
		if (valueTypeInfo == null)
			throw new IllegalArgumentException("The valueTypeInfo can not be null");

		this.content = content;
		this.type = valueTypeInfo;
	}

	public String getContent() {
		return content;
	}

	public ValueTypeInfo getValueTypeInfo() {
		return type;
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
