package umbc.ebiquity.kang.machinelearning.classification.evaluation;

import weka.core.Instances;

public interface ICrossValidationSplits {

	Instances[] getTrainingData();

	Instances[] getTestData();

	int getNumberOfSplit();

}
