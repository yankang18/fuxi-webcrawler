package umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp;

public class ValueTypeInfo {

	private ValueType valueType;
	private String unit;

	private ValueTypeInfo(ValueType valueType) {
		this.valueType = valueType;
	}

	/**
	 * @return the valueType
	 */
	public ValueType getValueType() {
		return valueType;
	}

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
		this.unit = unit;
	}
	
	public static ValueTypeInfo createValueTypeInfo(ValueType valueType, String unit) {
		ValueTypeInfo valueTypeInfo = new ValueTypeInfo(valueType);
		valueTypeInfo.setUnit(unit);
		return valueTypeInfo;
	}

	public static ValueTypeInfo createValueTypeInfo(ValueType valueType) {
		ValueTypeInfo valueTypeInfo = new ValueTypeInfo(valueType);
		return valueTypeInfo;
	}
}
