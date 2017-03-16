package umbc.ebiquity.kang.machinelearning.classification.impl;

import umbc.ebiquity.kang.machinelearning.classification.IClassifier;
import umbc.ebiquity.kang.machinelearning.classification.IPrediction;
import weka.classifiers.trees.J48;
import weka.core.Instance;

/**
 * This class serves as a wrapper of the Weka J48 classifier.
 * 
 * @author yankang
 *
 */
public class WekaJ48Classifier implements IClassifier<DataPoint> {

	private J48 classifier = new J48();

	private FeatureSchema featureSchema;

	/**
	 * 
	 * @param featureSchema
	 */
	public WekaJ48Classifier(FeatureSchema featureSchema) {
		this.featureSchema = featureSchema;
	}

	@Override
	public IPrediction predict(DataPoint dataPoint) throws Exception {
		Instance instance = WekaClassifierUtil.convertDataPointToInstance(dataPoint, featureSchema.getFeatureMetaData(),
				featureSchema.getClassFeatureName());
		double value = classifier.classifyInstance(instance);
		return new WekaPrediction(value);
	}

}
