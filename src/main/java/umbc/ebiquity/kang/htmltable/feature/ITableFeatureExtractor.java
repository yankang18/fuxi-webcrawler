package umbc.ebiquity.kang.htmltable.feature;

import umbc.ebiquity.kang.htmltable.IHTMLDataTable;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimiliartySuite;

public interface ITableFeatureExtractor {

	String getFeatureName();

	Object extractFeatureValue(IHTMLDataTable dataTable, ITableRecordsSimiliartySuite similaritySuite);

}
