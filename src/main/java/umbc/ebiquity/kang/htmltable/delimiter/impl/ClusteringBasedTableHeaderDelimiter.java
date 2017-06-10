package umbc.ebiquity.kang.htmltable.delimiter.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.impl.StandardHtmlElement;
import umbc.ebiquity.kang.htmltable.delimiter.ITableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.htmltable.util.HTMLTableValidator;

public class ClusteringBasedTableHeaderDelimiter implements ITableHeaderDelimiter {

    private ITableHeaderDelimiter verticalTableResolver;
    private ITableHeaderDelimiter horizontalTableResolver;
    
	public ClusteringBasedTableHeaderDelimiter() {
		verticalTableResolver = new ClusteringBasedVerticalTableHeaderDelimiter();
		horizontalTableResolver = new ClusteringBasedHorizontalTableHeaderDelimiter();
	}

	@Override
	public HeaderDelimitedTable delimit(Element element) {
		HTMLTableValidator.isTable(element); 
		
		HeaderDelimitedTable vResult = verticalTableResolver.delimit(element);
		HeaderDelimitedTable hResult = horizontalTableResolver.delimit(element);

		if (DataTableHeaderType.NonHeaderTable == vResult.getDataTableHeaderType()
				&& DataTableHeaderType.NonHeaderTable == hResult.getDataTableHeaderType()) {
			HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.NonHeaderTable);
			result.setHorizontalDataRecords(hResult.getHorizontalDataRecords());
			result.setVerticalDataRecords(vResult.getVerticalDataRecords());
			result.setHtmlElement(StandardHtmlElement.createDefaultStandardHtmlElement(element));
			return result;
		} else if (DataTableHeaderType.VerticalHeaderTable == vResult.getDataTableHeaderType()
				&& DataTableHeaderType.UnDetermined == hResult.getDataTableHeaderType()
				|| DataTableHeaderType.NonHeaderTable == hResult.getDataTableHeaderType()) {
			HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.VerticalHeaderTable);
			result.setVerticalHeaderRecords(vResult.getVerticalHeaderRecords());
			result.setVerticalDataRecords(vResult.getVerticalDataRecords());
			result.setHtmlElement(StandardHtmlElement.createDefaultStandardHtmlElement(element));
			return result;
		} else if (DataTableHeaderType.HorizontalHeaderTable == hResult.getDataTableHeaderType()
				&& (DataTableHeaderType.UnDetermined == vResult.getDataTableHeaderType()
						|| DataTableHeaderType.NonHeaderTable == vResult.getDataTableHeaderType())) {
			HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.HorizontalHeaderTable);
			result.setHorizontalHeaderRecords(hResult.getHorizontalHeaderRecords());
			result.setHorizontalDataRecords(hResult.getHorizontalDataRecords());
			result.setHtmlElement(StandardHtmlElement.createDefaultStandardHtmlElement(element));
			return result;
		} else if (DataTableHeaderType.VerticalHeaderTable == vResult.getDataTableHeaderType()
				&& DataTableHeaderType.HorizontalHeaderTable == hResult.getDataTableHeaderType()) {
			HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.TwoDirectionalHeaderTable);
			result.setVerticalHeaderRecords(vResult.getVerticalHeaderRecords());
			result.setVerticalDataRecords(vResult.getVerticalDataRecords());
			result.setHorizontalHeaderRecords(hResult.getHorizontalHeaderRecords());
			result.setHorizontalDataRecords(hResult.getHorizontalDataRecords());
			result.setHtmlElement(StandardHtmlElement.createDefaultStandardHtmlElement(element));
			return result;
		} else {
			HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.UnDetermined);
			result.setHtmlElement(StandardHtmlElement.createDefaultStandardHtmlElement(element));
			return result;
		}

	}
}
