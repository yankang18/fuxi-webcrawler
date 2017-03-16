package umbc.ebiquity.kang.machinelearning.classification.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FeatureMetaData {

	private String featureName; // the feature name
	private LinkedHashSet<String> nominalValues; // the nominal values
	public static final int STRING_LIST = 0;
	public static final int CSV_FORMAT = 1;
	private boolean isString;
	private boolean isNominal;
	private boolean isNumberic;

	public FeatureMetaData(String name) {
	    featureName = name;
		isNominal = name.startsWith("nominal_");
	}
	
	/**
	 * 
	 * @param data
	 */
	public void determineFeatureType(List<DataPoint> data) { 
		if (isNominal()) {
			generateNominalValues(data);
		} else {
			doDetermineType(data);
		}
	}

	private void doDetermineType(List<DataPoint> data) {
		int dataSize = data.size();
		int numOfNull = 0;
		int numOfString = 0;
		int numOfNumber = 0;
		for (DataPoint r : data) {
			Object value = r.getAttribute(featureName);
			if (value == null) {
				numOfNull++;
				// debug("Region " + r + " has no attribute: " + name);
			} else if (value instanceof String) {
				numOfString++;
			} else if (value instanceof Number) {
				numOfNumber++;
			}
		}
		if (numOfString + numOfNull == dataSize) {
			isNominal = false;
			isString = true;
			isNumberic = false;
		} else if (numOfNumber + numOfNull == dataSize) {
			isNominal = false;
			isString = false;
			isNumberic = true;
		} else if (numOfNull == dataSize) {
			isNominal = false;
			isString = false;
			isNumberic = false;
		} else {
			throw new IllegalArgumentException("The types of the data are not consistent.");
		}
	}

	/**
	 * 
	 * <p>
	 * Note: Calling this method will set the type of this feature as nomial.
	 * </p>
	 * 
	 * @param data
	 */
	public void generateNominalValues(List<DataPoint> data) {
		isNominal = true;
		if (nominalValues == null) {
			nominalValues = new LinkedHashSet<String>();
		}
		
		for (DataPoint dp : data) {
			Object value = dp.getAttribute(featureName);
			if (value == null) {
				// debug("Region " + r + " has no attribute: " + name);
			} else if (value instanceof String) {
				addNominalValue((String) value);
			} else if (value instanceof Number) {
				addNominalValue(value.toString());
			}
		}
	}

	public String getName() {
		return featureName;
	}

	public boolean isNominal() {
		return isNominal;
	}

	public Set<String> getNominalValues() {
		return nominalValues;
	}

	public boolean isString() {
		return isString;
	}

	public boolean isNumberic() {
		return isNumberic;
	}

	/**
	 * Adds a new nominal value.
	 * 
	 * @param s
	 *            the new value
	 */
	private void addNominalValue(String s) {
		nominalValues.add(s);
	}
}
