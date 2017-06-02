package umbc.ebiquity.kang.webtable.spliter.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.webtable.spliter.ITableHeaderSpliter;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;
import umbc.ebiquity.kang.webtable.util.HTMLTableValidator;

public class ClusteringBasedTableHeaderSpliter implements ITableHeaderSpliter {

    private ITableHeaderSpliter verticalTableResolver;
    private ITableHeaderSpliter horizontalTableResolver;
    
	public ClusteringBasedTableHeaderSpliter() {
		verticalTableResolver = new ClusteringBasedVerticalTableHeaderSpliter();
		horizontalTableResolver = new ClusteringBasedHorizontalTableHeaderSpliter();
	}

	@Override
	public TableSplitingResult split(Element element) {
		HTMLTableValidator.isTable(element); 
		
		TableSplitingResult vResult = verticalTableResolver.split(element);
		TableSplitingResult hResult = horizontalTableResolver.split(element);

		if (DataTableHeaderType.NonHeaderTable == vResult.getDataTableHeaderType()
				&& DataTableHeaderType.NonHeaderTable == hResult.getDataTableHeaderType()) {
			TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.NonHeaderTable);
			result.setHorizontalDataRecords(hResult.getHorizontalDataRecords());
			result.setVerticalDataRecords(vResult.getVerticalDataRecords());
			return result;
		} else if (DataTableHeaderType.VerticalHeaderTable == vResult.getDataTableHeaderType()
				&& DataTableHeaderType.UnDetermined == hResult.getDataTableHeaderType()
				|| DataTableHeaderType.NonHeaderTable == hResult.getDataTableHeaderType()) {
			TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.VerticalHeaderTable);
			result.setVerticalHeaderRecords(vResult.getVerticalHeaderRecords());
			result.setVerticalDataRecords(vResult.getVerticalDataRecords());
			return result;
		} else if (DataTableHeaderType.HorizontalHeaderTable == hResult.getDataTableHeaderType()
				&& (DataTableHeaderType.UnDetermined == vResult.getDataTableHeaderType()
						|| DataTableHeaderType.NonHeaderTable == vResult.getDataTableHeaderType())) {
			TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.HorizontalHeaderTable);
			result.setHorizontalHeaderRecords(hResult.getHorizontalHeaderRecords());
			result.setHorizontalDataRecords(hResult.getHorizontalDataRecords());
			return result;
		} else if (DataTableHeaderType.VerticalHeaderTable == vResult.getDataTableHeaderType()
				&& DataTableHeaderType.HorizontalHeaderTable == hResult.getDataTableHeaderType()) {
			TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.TwoDirectionalHeaderTable);
			result.setVerticalHeaderRecords(vResult.getVerticalHeaderRecords());
			result.setVerticalDataRecords(vResult.getVerticalDataRecords());
			result.setHorizontalHeaderRecords(hResult.getHorizontalHeaderRecords());
			result.setHorizontalDataRecords(hResult.getHorizontalDataRecords());
			return result;
		} else {
			TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.UnDetermined);
			return result;
		}

	}
}
