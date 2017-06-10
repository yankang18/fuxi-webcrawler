package umbc.ebiquity.kang.htmltable.feature.impl;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmltable.feature.ITableFeaturesExtractor;
import umbc.ebiquity.kang.machinelearning.classification.FileDataSetCreator;
import umbc.ebiquity.kang.machinelearning.classification.IFeatureData;

/**
 * Create testing data set from file
 * 
 * @author yankang
 */
public class TestingDataSetCreator extends FileDataSetCreator {

	private ITableFeaturesExtractor extractor;

	public TestingDataSetCreator(ITableFeaturesExtractor extractor, Set<String> featureNames) {
		super(featureNames);
		if (extractor == null)
			throw new IllegalArgumentException("The TableFeaturesExtractor can not be null");
		this.extractor = extractor;
	}

	@Override
	protected void extractFeatures(File file, FeaturesExtractionCallBack callBack) {
		String fileName = file.getName().toLowerCase().trim();
		if (!fileName.endsWith("_test.html")) {
			return;
		}

		try {
			Document doc = Jsoup.parse(file, "UTF-8");
			Element element = doc.getElementsByTag("table").get(0);
			IFeatureData features = extractor.extract(element);
			callBack.success(features, null);
		} catch (IOException e) {
			throw new IllegalStateException("");
		}
	}
}
