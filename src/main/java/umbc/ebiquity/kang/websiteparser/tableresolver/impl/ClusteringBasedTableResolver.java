package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.websiteparser.tableresolver.IClusteringBasedTableHeaderIdentifier;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableResolver;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.TableStatus;

public class ClusteringBasedTableResolver implements ITableResolver {

    private ITableResolver verticalTableResolver;
    private ITableResolver horizontalTableResolver;
    
	public ClusteringBasedTableResolver(IClusteringBasedTableHeaderIdentifier identifier) {
		verticalTableResolver = new ClusteringBasedVerticalTableResolver();
		horizontalTableResolver = new ClusteringBasedHorizontalTableResolver();
	}

	@Override
	public TableResolveResult resolve(Element element) {

		TableResolveResult vResult = verticalTableResolver.resolve(element);
		TableResolveResult hResult = horizontalTableResolver.resolve(element);

		if (DataTableHeaderType.NHT == vResult.getDataTableHeaderType()
				&& DataTableHeaderType.NHT == hResult.getDataTableHeaderType()) {
			TableResolveResult result = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.NHT);
			result.setHorizontalDataRecords(hResult.getHorizontalDataRecords());
			result.setVerticalDataRecords(vResult.getVerticalDataRecords());
			return result;
		} else if (DataTableHeaderType.VHT == vResult.getDataTableHeaderType()
				&& DataTableHeaderType.UD == hResult.getDataTableHeaderType()
				|| DataTableHeaderType.NHT == hResult.getDataTableHeaderType()) {
			TableResolveResult result = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.VHT);
			result.setVerticalHeaderRecords(vResult.getVerticalHeaderRecords());
			result.setVerticalDataRecords(vResult.getVerticalDataRecords());
			return result;
		} else if (DataTableHeaderType.HHT == hResult.getDataTableHeaderType()
				&& (DataTableHeaderType.UD == vResult.getDataTableHeaderType()
						|| DataTableHeaderType.NHT == vResult.getDataTableHeaderType())) {
			TableResolveResult result = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.HHT);
			result.setHorizontalHeaderRecords(hResult.getHorizontalHeaderRecords());
			result.setHorizontalDataRecords(hResult.getHorizontalDataRecords());
			return result;
		} else if (DataTableHeaderType.VHT == vResult.getDataTableHeaderType()
				&& DataTableHeaderType.HHT == hResult.getDataTableHeaderType()) {
			TableResolveResult result = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.TDH);
			result.setVerticalHeaderRecords(vResult.getVerticalHeaderRecords());
			result.setVerticalDataRecords(vResult.getVerticalDataRecords());
			result.setHorizontalHeaderRecords(hResult.getHorizontalHeaderRecords());
			result.setHorizontalDataRecords(hResult.getHorizontalDataRecords());
			return result;
		} else {
			TableResolveResult result = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.UD);
			return result;
		}

	}
}
