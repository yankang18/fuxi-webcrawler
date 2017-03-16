package umbc.ebiquity.kang.webtable.feature.impl;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.machinelearning.classification.FileDataSetCreator;
import umbc.ebiquity.kang.machinelearning.classification.IFeatureData;
import umbc.ebiquity.kang.webtable.feature.ITableFeaturesExtractor;

/**
 * Create training data set from file
 * 
 * @author yankang
 */
public class TrainingDataSetCreator extends FileDataSetCreator {

	private ITableFeaturesExtractor extractor;
	private Set<String> classLabels;

	public TrainingDataSetCreator(ITableFeaturesExtractor extractor, Set<String> featureNames,
			Set<String> classLabels) {
		super(featureNames);
		if (extractor == null)
			throw new IllegalArgumentException("The TableFeaturesExtractor can not be null");
		this.extractor = extractor;
		this.classLabels = classLabels;
	}

	protected void extractFeatures(File file, FeaturesExtractionCallBack callBack) {
		String fileName = file.getName().toLowerCase().trim();
		System.out.println("File Name: " + fileName);
		String classLabel = null;
		for (String cl : classLabels) {
			if (fileName.startsWith(cl)) {
				classLabel = cl;
				break;
			}
		}

		if (classLabel == null) {
			return;
		}
		try {
			Document doc = Jsoup.parse(file, "UTF-8");
			Element element = doc.getElementsByTag("table").get(0);
			IFeatureData features = extractor.extract(element);
			callBack.success(features, classLabel);
		} catch (IOException e) {
			throw new IllegalStateException("");
		}
	}
}
