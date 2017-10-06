package umbc.ebiquity.kang.machinelearning.classification.modelselection.impl;

import java.util.List;

import umbc.ebiquity.kang.machinelearning.classification.modelselection.ICrossValidationDetail;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;

public class CrossValidationDetail implements ICrossValidationDetail {

	private Evaluation[] evaluations;
	private Classifier[] classifiers;

	public CrossValidationDetail(Classifier[] classifiers, Evaluation[] evaluations) {
		
		if (classifiers == null || classifiers.length == 0) {
			throw new IllegalArgumentException("classifiers can not be null or empty");
		}
		
		if (evaluations == null || evaluations.length == 0) {
			throw new IllegalArgumentException("evaluations can not be null or empty");
		}
		this.evaluations = evaluations;
		this.classifiers = classifiers;
	}

	@Override
	public Classifier[] getClassifiers() {
		return classifiers;
	}

	@Override
	public Evaluation[] getEvaluations() {
		return evaluations;
	}

	@Override
	public double getOverallAccuracy() {
		double correct = 0;
		double totalNumOfPredictions = 0;
		for (int i = 0; i < evaluations.length; i++) {
			for (Prediction prediction : evaluations[i].predictions()) {
				totalNumOfPredictions++;
				if (prediction.predicted() == prediction.actual()) {
					correct++;
				}
			}
		}
		return 100 * correct / totalNumOfPredictions;
	}

	@Override
	public double[] getAccuracies() {
		int numOfEval = evaluations.length;
		double[] accuracies = new double[numOfEval];
		for (int i = 0; i < numOfEval; i++) {
			double correct = 0;
			List<Prediction> predictions = evaluations[i].predictions();
			for (Prediction prediction : predictions) {
				if (prediction.predicted() == prediction.actual()) {
					correct++;
				}
			}
			accuracies[i] = 100 * correct / predictions.size();
		}
		return accuracies;
	}

	@Override
	public int getNumberOfSplits() {
		return evaluations.length;
	}

}
