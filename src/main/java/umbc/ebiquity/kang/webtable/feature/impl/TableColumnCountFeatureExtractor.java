package umbc.ebiquity.kang.webtable.feature.impl;

import umbc.ebiquity.kang.webtable.IHTMLDataTable;
import umbc.ebiquity.kang.webtable.feature.ITableFeatureExtractor;
import umbc.ebiquity.kang.webtable.similarity.ITableRecordsSimiliartySuite;

public class TableColumnCountFeatureExtractor implements ITableFeatureExtractor {

	@Override
	public String getFeatureName() {
		return "NumberOfColumns";
	}

	@Override
	public Object extractFeatureValue(IHTMLDataTable dataTable, ITableRecordsSimiliartySuite similaritySuite) {
		return dataTable.getColumnCount();
	}

}
