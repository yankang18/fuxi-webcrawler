package umbc.ebiquity.kang.webtable.feature;

import java.util.Set;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.machinelearning.classification.IFeatureData;

public interface ITableFeaturesExtractor {

	IFeatureData extract(Element TableElement);

	Set<String> getFeatureNames(); 

}
