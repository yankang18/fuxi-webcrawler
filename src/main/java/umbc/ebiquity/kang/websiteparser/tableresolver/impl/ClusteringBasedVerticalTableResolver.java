package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.TableStatus;

public class ClusteringBasedVerticalTableResolver extends AbstractClusteringBasedTableResolver {
	
	public ClusteringBasedVerticalTableResolver() {
		super();
	}

	@Override
	protected HTMLDataTable convertToDataTable(Element tableElem) {
		return HTMLDataTable.createVerticalDataTable(tableElem);
	}
	
	@Override
	protected TableResolveResult doResolveTable(List<TableRecord> cluster1, List<TableRecord> cluster2) {
		if (cluster1.get(0).getSequenceNumber() < cluster2.get(0).getSequenceNumber()) {
			validateClusterSequence(cluster1, cluster2);
			TableResolveResult result = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.VHT);
			result.setVerticalHeaderRecords(cluster1);
			result.setVerticalDataRecords(cluster2);
			return result;

		} else {
			validateClusterSequence(cluster2, cluster1);
			TableResolveResult result = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.VHT);
			result.setVerticalHeaderRecords(cluster2);
			result.setVerticalDataRecords(cluster1);
			return result;
		}
	}

	@Override
	protected TableResolveResult doResolveTable(List<TableRecord> cluster) {
		TableResolveResult resolveResult = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.NHT);
		resolveResult.setVerticalDataRecords(cluster);
		return resolveResult;
	}

}
