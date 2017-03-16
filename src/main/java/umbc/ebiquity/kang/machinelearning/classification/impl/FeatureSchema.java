package umbc.ebiquity.kang.machinelearning.classification.impl;

import java.util.Set;

public class FeatureSchema {
	
	private Set<FeatureMetaData> features;
	private String classFeatureName;

	public FeatureSchema(Set<FeatureMetaData> features, String classFeatureName) {
		this.features = features;
		this.classFeatureName = classFeatureName;
	}

	/**
	 * @return the features
	 */
	public Set<FeatureMetaData> getFeatureMetaData() {
		return features;
	}

	/**
	 * @return the classLabel
	 */
	public String getClassFeatureName() {
		return classFeatureName;
	}
}
