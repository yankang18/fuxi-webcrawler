package umbc.ebiquity.kang.webtable.spliter.impl;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.webtable.spliter.AbstractHTMLHeaderTagBasedTableSpliter;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;

public class HTMLHeaderTagBasedVerticalTableSpliter extends AbstractHTMLHeaderTagBasedTableSpliter {

	private HTMLHeaderTagBasedTableHeaderLocator headerResolver;

	public HTMLHeaderTagBasedVerticalTableSpliter() {
		headerResolver = new HTMLHeaderTagBasedTableHeaderLocator();
	}
	
	@Override
	public TableSplitingResult split(Element element) {
		HTMLTableValidator.validateTableElement(element); 
		
		return resolveHeaderFromBody(element, new IHeaderLocatingBorker() {
			@Override
			public TableHeaderLocatingResult locateHeader(Elements elements) {
				return headerResolver.locateVeriticalHeader(elements);
			}
		});
	}

	@Override
	protected TableSplitingResult convertToTableResolveResult(Element element, TableHeaderLocatingResult result) {
		
		if (DataTableHeaderType.VHT == result.getDataTableType()) {

			HeaderPosition position = result.getVerticalHeaderPosition();

			List<TableRecord> headerRecords = HTMLTableRecordsCreator.createVerticalTableRecords(element,
					position.getRowStart(), position.getRowEnd(), position.getColumnBorderCount());
			List<TableRecord> dataRecords = HTMLTableRecordsCreator.createVerticalTableRecords(element,
					position.getRowEnd() + 1, position.getRowBorderCount() - 1, position.getColumnBorderCount());

			TableSplitingResult resolveResult = new TableSplitingResult(result.getTableStatus(),
					result.getDataTableType());
			resolveResult.setVerticalHeaderRecords(headerRecords);
			resolveResult.setVerticalDataRecords(dataRecords);
			return resolveResult;

		} else {
			TableSplitingResult resolveResult = new TableSplitingResult(result.getTableStatus(),
					result.getDataTableType());
			return resolveResult;
		}
	}
}
