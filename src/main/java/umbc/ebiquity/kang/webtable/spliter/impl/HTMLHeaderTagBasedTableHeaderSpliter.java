package umbc.ebiquity.kang.webtable.spliter.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.webtable.spliter.ITableHeaderSpliter;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;
import umbc.ebiquity.kang.webtable.util.HTMLTableValidator;

/**
 * This class separates header records and data records based HTML tags such as
 * thead and th.
 * 
 * @author yankang
 */
public class HTMLHeaderTagBasedTableHeaderSpliter implements ITableHeaderSpliter {

	private ITableHeaderSpliter verticalTableResolver;
	private ITableHeaderSpliter horizontalTableResolver;

	public HTMLHeaderTagBasedTableHeaderSpliter() {
		verticalTableResolver = new HTMLHeaderTagBasedVerticalTableHeaderSpliter();
		horizontalTableResolver = new HTMLHeaderTagBasedHorizontalTableHeaderSpliter();
	}

	@Override
	public TableSplitingResult split(Element element) {
		HTMLTableValidator.isTable(element); 
		
		TableSplitingResult result1 = verticalTableResolver.split(element);
		TableSplitingResult result2 = horizontalTableResolver.split(element);

		if (result1.getTableStatus() == TableStatus.UnRegularTable
				&& result2.getTableStatus() == TableStatus.UnRegularTable) {
			return new TableSplitingResult(TableStatus.UnRegularTable, DataTableHeaderType.UnDetermined);
		}

		System.out.println("Point 1");
		if (result1.getDataTableHeaderType() == DataTableHeaderType.VerticalHeaderTable
				&& result2.getDataTableHeaderType() == DataTableHeaderType.HorizontalHeaderTable) {
			TableSplitingResult result = new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.TwoDirectionalHeaderTable);
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
			return new TableSplitingResult(TableStatus.RegularTable, DataTableHeaderType.UnDetermined);
		}
	}

}
