package umbc.ebiquity.kang.webtable.spliter.impl;

import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.webtable.spliter.AbstractClusteringBasedTableSpliter;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;

public class ClusteringBasedHorizontalTableSpliter extends AbstractClusteringBasedTableSpliter {

	public ClusteringBasedHorizontalTableSpliter() {
		super();
	}
	
	@Override
	protected HTMLDataTable convertToDataTable(Element tableElem) {
		return HTMLDataTable.createHorizontalDataTable(tableElem);
	}
	
	@Override
	protected TableSplitingResult doResolveTable(List<TableRecord> cluster1, List<TableRecord> cluster2) {
		if (cluster1.get(0).getSequenceNumber() < cluster2.get(0).getSequenceNumber()) {
			if (validateClusterSequence(cluster1, cluster2)) {
				TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.HHT);
				result.setHorizontalHeaderRecords(cluster1);
				result.setHorizontalDataRecords(cluster2);
				return result;
			} else {
				TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.UD);
				return result;
			}

		} else {
			if (validateClusterSequence(cluster2, cluster1)) {
				TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.HHT);
				result.setHorizontalHeaderRecords(cluster2);
				result.setHorizontalDataRecords(cluster1);
				return result;
			} else {
				TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.UD);
				return result;
			}
		}
	}

	@Override
	protected TableSplitingResult doResolveTable(List<TableRecord> cluster) {
		TableSplitingResult resolveResult = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.NHT);
		resolveResult.setHorizontalDataRecords(cluster);
		return resolveResult;
	}
}
