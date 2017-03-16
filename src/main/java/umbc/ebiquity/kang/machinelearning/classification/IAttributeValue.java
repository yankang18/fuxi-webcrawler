package umbc.ebiquity.kang.machinelearning.classification;

/**
 * This interface represents the value of an attribute for a classifier.
 * 
 * @author yankang
 *
 */
public interface IAttributeValue {

	/**
	 * This enum defines the type of attribute value.
	 *
	 */
	public enum AttributeValueType {
		String, Number
	}

	/**
	 * Get the type of this attribute value
	 * 
	 * @return an <code>AttributeValueType</code>
	 */
	AttributeValueType getType();

	/**
	 * Get the attribute value.
	 * 
	 * @return an Object representing the value
	 */
	Object getValue();

}
