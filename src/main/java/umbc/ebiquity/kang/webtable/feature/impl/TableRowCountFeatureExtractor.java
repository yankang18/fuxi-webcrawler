package umbc.ebiquity.kang.webtable.feature.impl;

import umbc.ebiquity.kang.webtable.IHTMLDataTable;
import umbc.ebiquity.kang.webtable.feature.ITableFeatureExtractor;
import umbc.ebiquity.kang.webtable.similarity.ITableRecordsSimiliartySuite;

public class TableRowCountFeatureExtractor implements ITableFeatureExtractor {

	@Override
	public String getFeatureName() {
		return "NumberOfRows";
	}

	@Override
	public Object extractFeatureValue(IHTMLDataTable dataTable, ITableRecordsSimiliartySuite similaritySuite) {
		return dataTable.getRowCount();
	}

}
