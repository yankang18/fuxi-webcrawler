package umbc.ebiquity.kang.webtable.feature;

import umbc.ebiquity.kang.webtable.IHTMLDataTable;
import umbc.ebiquity.kang.webtable.similarity.ITableRecordsSimiliartySuite;

public interface ITableFeatureExtractor {

	String getFeatureName();

	Object extractFeatureValue(IHTMLDataTable dataTable, ITableRecordsSimiliartySuite similaritySuite);

}
