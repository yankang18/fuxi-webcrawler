package umbc.ebiquity.kang.webtable.spliter.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.webtable.spliter.ITableSpliter;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;

public class ClusteringBasedTableSpliter implements ITableSpliter {

    private ITableSpliter verticalTableResolver;
    private ITableSpliter horizontalTableResolver;
    
	public ClusteringBasedTableSpliter() {
		verticalTableResolver = new ClusteringBasedVerticalTableSpliter();
		horizontalTableResolver = new ClusteringBasedHorizontalTableSpliter();
	}

	@Override
	public TableSplitingResult split(Element element) {
		HTMLTableValidator.validateTableElement(element); 
		
		TableSplitingResult vResult = verticalTableResolver.split(element);
		TableSplitingResult hResult = horizontalTableResolver.split(element);

		if (DataTableHeaderType.NHT == vResult.getDataTableHeaderType()
				&& DataTableHeaderType.NHT == hResult.getDataTableHeaderType()) {
			TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.NHT);
			result.setHorizontalDataRecords(hResult.getHorizontalDataRecords());
			result.setVerticalDataRecords(vResult.getVerticalDataRecords());
			return result;
		} else if (DataTableHeaderType.VHT == vResult.getDataTableHeaderType()
				&& DataTableHeaderType.UD == hResult.getDataTableHeaderType()
				|| DataTableHeaderType.NHT == hResult.getDataTableHeaderType()) {
			TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.VHT);
			result.setVerticalHeaderRecords(vResult.getVerticalHeaderRecords());
			result.setVerticalDataRecords(vResult.getVerticalDataRecords());
			return result;
		} else if (DataTableHeaderType.HHT == hResult.getDataTableHeaderType()
				&& (DataTableHeaderType.UD == vResult.getDataTableHeaderType()
						|| DataTableHeaderType.NHT == vResult.getDataTableHeaderType())) {
			TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.HHT);
			result.setHorizontalHeaderRecords(hResult.getHorizontalHeaderRecords());
			result.setHorizontalDataRecords(hResult.getHorizontalDataRecords());
			return result;
		} else if (DataTableHeaderType.VHT == vResult.getDataTableHeaderType()
				&& DataTableHeaderType.HHT == hResult.getDataTableHeaderType()) {
			TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.TDH);
			result.setVerticalHeaderRecords(vResult.getVerticalHeaderRecords());
			result.setVerticalDataRecords(vResult.getVerticalDataRecords());
			result.setHorizontalHeaderRecords(hResult.getHorizontalHeaderRecords());
			result.setHorizontalDataRecords(hResult.getHorizontalDataRecords());
			return result;
		} else {
			TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.UD);
			return result;
		}

	}
}
