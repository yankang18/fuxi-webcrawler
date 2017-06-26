package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl;

public class HTMLTreeNodeValue {

	public enum ValueType {
		Number, NumberPhrase, Image, Term, Paragraph; /* one or more sentences */

		private String unit;

		/**
		 * @return the unit
		 */
		public String getUnit() {
			return unit;
		}

		/**
		 * @param unit
		 *            the unit to set
		 */
		public void setUnit(String unit) {
			this.unit = unit.trim();
		}
	}

	private String content;
	private ValueType type;
	private String description;

	public HTMLTreeNodeValue(String content, ValueType type) {
		this.content = content;
		this.type = type;
	}

	public String getValue() {
		return content;
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
