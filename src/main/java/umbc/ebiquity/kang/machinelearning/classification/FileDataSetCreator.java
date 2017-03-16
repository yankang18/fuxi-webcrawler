package umbc.ebiquity.kang.machinelearning.classification;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import umbc.ebiquity.kang.machinelearning.classification.impl.DataPoint;
import umbc.ebiquity.kang.machinelearning.classification.impl.DataSet;

public abstract class FileDataSetCreator {
	
	public final static String CLASS_ATTRIBUTE_NAME = "class";

	private Set<String> featureNames;

	protected FileDataSetCreator(Set<String> featureNames) {
		if (featureNames == null || featureNames.size() == 0)
			throw new IllegalArgumentException("The featureNames can not be null and empty");
		this.featureNames = featureNames;
	}

	public DataSet createDataSet(File directory) throws IOException {
		if (!directory.isDirectory())
			throw new IllegalArgumentException("The directory can not be a file");

		DataSet trainDataSet = new DataSet(featureNames, CLASS_ATTRIBUTE_NAME);

		for (File file : directory.listFiles()) {
			// TODO: May replace this call back mechanism (i.e., via interface)
			// with lambda function.
			extractFeatures(file, new FeaturesExtractionCallBack() {
				@Override
				public void success(IFeatureData dataFeatures, String classLabel) {
					trainDataSet.addDataPoint(createDatum(dataFeatures, classLabel));
				}
			});
		}
		return trainDataSet;
	}

	private DataPoint createDatum(IFeatureData features, String classLabel) {
		DataPoint dataPoint = new DataPoint();
		for (String fn : features.getFeatureNames()) {
			dataPoint.setAttribute(fn, features.getFeatureValue(fn));
		}
		if (classLabel != null) {
			dataPoint.setAttribute(CLASS_ATTRIBUTE_NAME, classLabel);
		}
		return dataPoint;
	}

	public static void print(IFeatureData features) {
		for (String fn : features.getFeatureNames()) {
			System.out.println(fn + ": " + features.getFeatureValue(fn));
		}
	}

	/**
	 * Extracts features and class label, if any, from the content in the
	 * specified file.
	 * 
	 * @param file
	 *            the file from where the features and class label are extracted
	 * @param callBack
	 *            the {@link FeaturesExtractionCallBack} interface serves as a
	 *            call back mechanism when the feature extraction process is
	 *            done
	 */
	protected abstract void extractFeatures(File file, FeaturesExtractionCallBack callBack);

	/**
	 * This interface defines the call back behavior when the feature extraction
	 * process is done.
	 * 
	 * @author yankang
	 */
	protected interface FeaturesExtractionCallBack {

		/**
		 * Triggers the call back mechanism.
		 * 
		 * @param dataFeatures
		 *            the extracted data features, can not be null
		 * @param classLabel
		 *            the extracted class label, can be null if no class label
		 *            was extracted
		 */
		void success(IFeatureData dataFeatures, String classLabel);
	}
}
