package umbc.ebiquity.kang.webtable.delimiter.impl;

import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.webtable.core.HTMLDataTable;
import umbc.ebiquity.kang.webtable.core.TableRecord;
import umbc.ebiquity.kang.webtable.delimiter.AbstractClusteringBasedTableHeaderDelimiter;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.TableStatus;

public class ClusteringBasedHorizontalTableHeaderDelimiter extends AbstractClusteringBasedTableHeaderDelimiter {

	public ClusteringBasedHorizontalTableHeaderDelimiter() {
		super();
	}
	
	@Override
	protected HTMLDataTable convertToDataTable(Element tableElem) {
		return HTMLDataTable.convertToHorizontalDataTable(tableElem);
	}
	
	@Override
	protected HeaderDelimitedTable doResolveTable(List<TableRecord> cluster1, List<TableRecord> cluster2) {
		if (cluster1.get(0).getSequenceNumber() < cluster2.get(0).getSequenceNumber()) {
			if (validateClusterSequence(cluster1, cluster2)) {
				HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.HorizontalHeaderTable);
				result.setHorizontalHeaderRecords(cluster1);
				result.setHorizontalDataRecords(cluster2);
				return result;
			} else {
				HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.UnDetermined);
				return result;
			}

		} else {
			if (validateClusterSequence(cluster2, cluster1)) {
				HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.HorizontalHeaderTable);
				result.setHorizontalHeaderRecords(cluster2);
				result.setHorizontalDataRecords(cluster1);
				return result;
			} else {
				HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.UnDetermined);
				return result;
			}
		}
	}

	@Override
	protected HeaderDelimitedTable doResolveTable(List<TableRecord> cluster) {
		HeaderDelimitedTable resolveResult = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.NonHeaderTable);
		resolveResult.setHorizontalDataRecords(cluster);
		return resolveResult;
	}
}
