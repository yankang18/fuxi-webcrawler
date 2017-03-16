package umbc.ebiquity.kang.machinelearning.classification.evaluation.impl;

import umbc.ebiquity.kang.machinelearning.classification.evaluation.ICrossValidationSplits;
import weka.core.Instances;

public class CrossValidationSplit implements ICrossValidationSplits {

	private Instances[] trainingData;
	private Instances[] testData;

	public CrossValidationSplit(Instances[] trainingData, Instances[] testData) {
		this.trainingData = trainingData;
		this.testData = testData;
	}

	@Override
	public Instances[] getTrainingData() {
		return trainingData;
	}

	@Override
	public Instances[] getTestData() {
		return testData;
	}
	
	@Override
	public int getNumberOfSplit() {
		return trainingData.length;
	}
}
