package umbc.ebiquity.kang.machinelearning.classification.modelselection;

import weka.core.Instances;

public interface ICrossValidationSplits {

	Instances[] getTrainingData();

	Instances[] getTestData();

	int getNumberOfSplit();

}
