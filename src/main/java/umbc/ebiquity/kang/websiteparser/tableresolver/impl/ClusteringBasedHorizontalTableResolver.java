package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.TableStatus;

public class ClusteringBasedHorizontalTableResolver extends AbstractClusteringBasedTableResolver {

	public ClusteringBasedHorizontalTableResolver() {
		super();
	}
	
	@Override
	protected HTMLDataTable convertToDataTable(Element tableElem) {
		return HTMLDataTable.createHorizontalDataTable(tableElem);
	}
	
	@Override
	protected TableResolveResult doResolveTable(List<TableRecord> cluster1, List<TableRecord> cluster2) {
		if (cluster1.get(0).getSequenceNumber() < cluster2.get(0).getSequenceNumber()) {
			validateClusterSequence(cluster1, cluster2);
			TableResolveResult result = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.HHT);
			result.setHorizontalHeaderRecords(cluster1);
			result.setHorizontalDataRecords(cluster2);
			return result;

		} else {
			validateClusterSequence(cluster2, cluster1);
			TableResolveResult result = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.HHT);
			result.setHorizontalHeaderRecords(cluster2);
			result.setHorizontalDataRecords(cluster1);
			return result;
		}
	}

	@Override
	protected TableResolveResult doResolveTable(List<TableRecord> cluster) {
		TableResolveResult resolveResult = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.NHT);
		resolveResult.setHorizontalDataRecords(cluster);
		return resolveResult;
	}
}
