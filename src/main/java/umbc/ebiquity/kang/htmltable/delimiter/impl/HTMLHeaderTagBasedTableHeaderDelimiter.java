package umbc.ebiquity.kang.htmltable.delimiter.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmltable.delimiter.ITableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.htmltable.util.HTMLTableValidator;

/**
 * This class separates header records and data records based HTML tags such as
 * thead and th.
 * 
 * @author yankang
 */
public class HTMLHeaderTagBasedTableHeaderDelimiter implements ITableHeaderDelimiter {

	private ITableHeaderDelimiter verticalTableDelimiter;
	private ITableHeaderDelimiter horizontalTableDelimiter;

	public HTMLHeaderTagBasedTableHeaderDelimiter() {
		verticalTableDelimiter = new HTMLHeaderTagBasedVerticalTableHeaderDelimiter();
		horizontalTableDelimiter = new HTMLHeaderTagBasedHorizontalTableHeaderDelimiter();
	}

	@Override
	public HeaderDelimitedTable delimit(Element element) {
		HTMLTableValidator.isTable(element); 
		
		HeaderDelimitedTable result1 = verticalTableDelimiter.delimit(element);
		HeaderDelimitedTable result2 = horizontalTableDelimiter.delimit(element);

		if (result1.getTableStatus() == TableStatus.UnRegularTable
				&& result2.getTableStatus() == TableStatus.UnRegularTable) {
			return new HeaderDelimitedTable(TableStatus.UnRegularTable, DataTableHeaderType.UnDetermined);
		}

		System.out.println("Point 1");
		if (result1.getDataTableHeaderType() == DataTableHeaderType.VerticalHeaderTable
				&& result2.getDataTableHeaderType() == DataTableHeaderType.HorizontalHeaderTable) {
			HeaderDelimitedTable result = new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.TwoDirectionalHeaderTable);
			result.setVerticalHeaderRecords(result1.getVerticalHeaderRecords());
			result.setVerticalDataRecords(result1.getVerticalDataRecords());
			result.setHorizontalHeaderRecords(result2.getHorizontalHeaderRecords());
			result.setHorizontalDataRecords(result2.getHorizontalDataRecords());
			System.out.println("Point 2");
			return result;
		} else if (result1.getDataTableHeaderType() == DataTableHeaderType.VerticalHeaderTable) {
			System.out.println("Point 3");
			return result1;
		} else if (result2.getDataTableHeaderType() == DataTableHeaderType.HorizontalHeaderTable) {
			System.out.println("Point 4");
			return result2;
		} else {
			return new HeaderDelimitedTable(TableStatus.RegularTable, DataTableHeaderType.UnDetermined);
		}
	}

}
