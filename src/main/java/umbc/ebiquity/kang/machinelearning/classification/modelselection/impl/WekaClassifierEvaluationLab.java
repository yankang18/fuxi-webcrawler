package umbc.ebiquity.kang.machinelearning.classification.modelselection.impl;

import java.util.Random;

import umbc.ebiquity.kang.machinelearning.classification.modelselection.ICrossValidationDetail;
import umbc.ebiquity.kang.machinelearning.classification.modelselection.ICrossValidationSplits;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class WekaClassifierEvaluationLab {

	/**
	 * Create cross-validation splits.
	 * 
	 * @param dataSet
	 *            the data set to be split
	 * @param numberOfFolds
	 *            the number of folds used to split the data set
	 * @param shuffle
	 *            true if shuffle the data set and the training data in each
	 *            split
	 * @return <code>ICrossValidationSplits</code>
	 */
	public static ICrossValidationSplits createCrossValidationSplits(Instances dataSet, int numberOfFolds, boolean shuffle) {

		if (shuffle) {
			dataSet = new Instances(dataSet);
			dataSet.randomize(new Random(1));
		}

		Instances[] trainingData = new Instances[numberOfFolds];
		Instances[] testData = new Instances[numberOfFolds];

		for (int i = 0; i < numberOfFolds; i++) {
			trainingData[i] = shuffle ? dataSet.trainCV(numberOfFolds, i, new Random(1))
									  : dataSet.trainCV(numberOfFolds, i);
			testData[i] = dataSet.testCV(numberOfFolds, i);
		}
		CrossValidationSplit crossValidation = new CrossValidationSplit(trainingData, testData);
		return crossValidation;

	}

	/**
	 * Cross-validate the specified classifier based on the specified
	 * cross-validation splits.
	 * 
	 * @param classifier
	 *            the classifier to be validated
	 * @param crossValidationSplits
	 *            the cross validation splits to validate the classifier
	 * @return <code>ICrossValidationDetail</code>
	 * @throws Exception
	 */
	public static ICrossValidationDetail crossValidate(Classifier classifier, ICrossValidationSplits crossValidationSplits) throws Exception {
		Instances[] trainingData = crossValidationSplits.getTrainingData();
		Instances[] testData = crossValidationSplits.getTestData();
		int numberOfSplit = crossValidationSplits.getNumberOfSplit();
		Evaluation[] evaluations = new Evaluation[numberOfSplit];
		Classifier[] classifiers = new Classifier[numberOfSplit];
		for (int i = 0; i < numberOfSplit; i++) {
			Evaluation evaluation = new Evaluation(trainingData[i]);
			Classifier copiedClassifier = AbstractClassifier.makeCopy(classifier);
			copiedClassifier.buildClassifier(trainingData[i]);
			evaluation.evaluateModel(copiedClassifier, testData[i]);
			evaluations[i] = evaluation;
			classifiers[i] = copiedClassifier;
		}
		CrossValidationDetail detail = new CrossValidationDetail(classifiers, evaluations);
		return detail;
	}

	public static Evaluation crossValidate(Classifier classifier, Instances dataSet, int numberOfFolds) throws Exception {
		Evaluation evaluation = new Evaluation(dataSet);
		Random rand = new Random(1); // using seed = 1
		evaluation.crossValidateModel(classifier, dataSet, numberOfFolds, rand);
		return evaluation;
	}

}
