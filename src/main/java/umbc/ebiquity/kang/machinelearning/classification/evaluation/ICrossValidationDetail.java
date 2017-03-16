package umbc.ebiquity.kang.machinelearning.classification.evaluation;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;

public interface ICrossValidationDetail {

	Evaluation[] getEvaluations();
	
	double getOverallAccuracy();

	double[] getAccuracies();

	int getNumberOfSplits();

	Classifier[] getClassifiers();

}
