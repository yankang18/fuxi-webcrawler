package umbc.ebiquity.kang.webtable.spliter.impl;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.webtable.core.HTMLTableRecordsParser;
import umbc.ebiquity.kang.webtable.core.HTMLTableValidator;
import umbc.ebiquity.kang.webtable.core.TableRecord;
import umbc.ebiquity.kang.webtable.spliter.AbstractHTMLHeaderTagBasedTableSpliter;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;

public class HTMLHeaderTagBasedVerticalTableHeaderSpliter extends AbstractHTMLHeaderTagBasedTableSpliter {

	private HTMLHeaderTagBasedTableHeaderLocator headerResolver;

	public HTMLHeaderTagBasedVerticalTableHeaderSpliter() {
		headerResolver = new HTMLHeaderTagBasedTableHeaderLocator();
	}
	
	@Override
	public TableSplitingResult split(Element element) {
		HTMLTableValidator.isTable(element); 
		
		return resolveHeaderFromBody(element, new IHeaderLocatingBorker() {
			@Override
			public TableHeaderLocatingResult locateHeader(Elements elements) {
				return headerResolver.locateVeriticalHeader(elements);
			}
		});
	}

	@Override
	protected TableSplitingResult convertToTableResolveResult(Element element, TableHeaderLocatingResult result) {
		
		if (DataTableHeaderType.VerticalHeaderTable == result.getDataTableType()) {

			HeaderPosition position = result.getVerticalHeaderPosition();

			List<TableRecord> headerRecords = HTMLTableRecordsParser.createTableRecordsFromVeriticalTableElement(element,
					position.getRowStart(), position.getRowEnd(), position.getColumnBorderCount());
			List<TableRecord> dataRecords = HTMLTableRecordsParser.createTableRecordsFromVeriticalTableElement(element,
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
