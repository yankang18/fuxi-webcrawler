package umbc.ebiquity.kang.machinelearning.classification;

import java.util.Set;

/**
 * This class holds a set of features and their corresponding values.
 * 
 * @author yankang
 *
 */
public interface IFeatureData {

	/**
	 * Get a set of feature names
	 * 
	 * @return a set holding feature names
	 */
	Set<String> getFeatureNames();

	/**
	 * Get feature value of a specified feature.
	 * 
	 * @param featureName
	 *            the feature name
	 * @return an Object representing value of the specified feature
	 */
	Object getFeatureValue(String featureName);

}
