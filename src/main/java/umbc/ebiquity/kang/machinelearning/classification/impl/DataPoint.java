package umbc.ebiquity.kang.machinelearning.classification.impl;

import java.util.HashMap;
import java.util.Map;

public class DataPoint {

	private Map<String, Object> attributes;

	public DataPoint() {
		attributes = new HashMap<String, Object>();
	}

	/**
	 * Retrieves an attribute.
	 * 
	 * @param name
	 *            the name of the requested attribute
	 * @return the attribute value or null if it does not exist
	 */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	/**
	 * Determines if the region has a non-null attribute for a given attribute
	 * name
	 * 
	 * @param name
	 *            the attribute name
	 * @return true if the attribute exists and is non-null
	 */
	public Boolean hasAttribute(String name) {
		return attributes.containsKey(name) && this.attributes.get(name) != null;
	}

	/**
	 * Sets the value for a new or existing attribute.
	 * 
	 * @param name
	 *            the attribute name
	 * @param value
	 *            the new value
	 */
	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	/**
	 * Gets the size attributes in this Datum.
	 * 
	 * @return the size of attributes
	 */
	public int sizeOfAttributes() {
		return attributes.size();
	}

}
