package umbc.ebiquity.kang.machinelearning.classification.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DataSet {

	private Set<String> featureNames; // The required feature
										// names
	private Set<FeatureMetaData> features; // The extracted feature objects
	private List<DataPoint> data; // Associated data points
	private String classAttribute; // The name of the class attribute (if
									// any)
	
	private boolean dataChanged;

	/**
	 * Constructs a Data Set.
	 */
	public DataSet(Set<String> featureNames, String classAttribute) {
		if (featureNames == null || featureNames.size() == 0) {
			throw new IllegalArgumentException("feature names can not be null");
		}

		if (classAttribute == null || classAttribute.trim().equals("")) {
			throw new IllegalArgumentException("class attribute can not be null");
		}

		this.classAttribute = classAttribute;
		this.featureNames = featureNames;
		this.features = new LinkedHashSet<FeatureMetaData>();
		this.data = new ArrayList<DataPoint>();
		this.dataChanged = false;
	}

	/**
	 * Sets the associated data points.
	 * 
	 * @param words
	 *            the data points
	 */
	public void setDataPoints(List<DataPoint> data) {
		for (DataPoint datum : data) {
			validateDatum(datum);
			data.add(datum);
		}
		dataChanged = true;
	}

	/**
	 * Inserts a data points into the feature set.
	 * 
	 * @param datum
	 *            the data point
	 */
	public void addDataPoint(DataPoint datum) {
		validateDatum(datum);
		data.add(datum);
		dataChanged = true;
	}

	/**
	 * 
	 * @param datum
	 */
	private void validateDatum(DataPoint datum) {
		// if (datum.sizeOfAttributes() != featureNames.size() + 1) {
		// throw new IllegalArgumentException("");
		// }
		//
		// for (String featureName : featureNames) {
		// if (!datum.hasAttribute(featureName)) {
		// throw new IllegalArgumentException("");
		// }
		// }
		if (!datum.hasAttribute(classAttribute)) {
			throw new IllegalArgumentException("The datum must have a class attribute.");
		}
	}
	
	public Set<FeatureMetaData> getFeatures(){
		constructFeatures();
		return features;
	}

	/**
	 * Constructs the set of features based on the data stored in this data set.
	 * <p/>
	 * Note: feature names that start with "nominal_" are treated as nominal
	 * features. Valid nominal values are constructed from all values
	 * represented in the associated data points.
	 * <p/>
	 * The class attribute is always treated as a nominal feature.
	 */
	private void constructFeatures() {
		if (dataChanged) {
			features.clear();
			for (String feature : featureNames) {
				FeatureMetaData f = new FeatureMetaData(feature);
				f.determineFeatureType(data);
				features.add(f);
			}

			if (classAttribute != null) {
				FeatureMetaData class_feature = new FeatureMetaData(classAttribute);
				class_feature.generateNominalValues(data);
				features.add(class_feature);
			}
			// Reset the dataChanged indicator
			dataChanged = false;
		}
	}

	/**
	 * Retrieves the class attribute.
	 * 
	 * @return the class attribute
	 */
	public String getClassAttribute() {
		return classAttribute;
	}
	
	public List<DataPoint> getData(){
		return data;
	}
}
