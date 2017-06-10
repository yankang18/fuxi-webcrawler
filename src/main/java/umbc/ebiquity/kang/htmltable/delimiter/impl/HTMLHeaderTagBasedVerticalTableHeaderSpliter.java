package umbc.ebiquity.kang.htmltable.delimiter.impl;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.htmltable.core.HTMLTableRecordsParser;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.delimiter.AbstractHTMLHeaderTagBasedTableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.util.HTMLTableValidator;

public class HTMLHeaderTagBasedVerticalTableHeaderSpliter extends AbstractHTMLHeaderTagBasedTableHeaderDelimiter {

	private HTMLHeaderTagBasedTableHeaderLocator headerResolver;

	public HTMLHeaderTagBasedVerticalTableHeaderSpliter() {
		headerResolver = new HTMLHeaderTagBasedTableHeaderLocator();
	}
	
	@Override
	public HeaderDelimitedTable delimit(Element element) {
		HTMLTableValidator.isTable(element); 
		
		return resolveHeaderFromBody(element, new IHeaderLocatingBorker() {
			@Override
			public TableHeaderLocatingResult locateHeader(Elements elements) {
				return headerResolver.locateVeriticalHeader(elements);
			}
		});
	}

	@Override
	protected HeaderDelimitedTable convertToTableResolveResult(Element element, TableHeaderLocatingResult result) {
		
		if (DataTableHeaderType.VerticalHeaderTable == result.getDataTableType()) {

			HeaderPosition position = result.getVerticalHeaderPosition();

			List<TableRecord> headerRecords = HTMLTableRecordsParser.createTableRecordsFromVeriticalTableElement(element,
					position.getRowStart(), position.getRowEnd(), position.getColumnBorderCount());
			List<TableRecord> dataRecords = HTMLTableRecordsParser.createTableRecordsFromVeriticalTableElement(element,
					position.getRowEnd() + 1, position.getRowBorderCount() - 1, position.getColumnBorderCount());

			HeaderDelimitedTable resolveResult = new HeaderDelimitedTable(result.getTableStatus(),
					result.getDataTableType());
			resolveResult.setVerticalHeaderRecords(headerRecords);
			resolveResult.setVerticalDataRecords(dataRecords);
			return resolveResult;

		} else {
			HeaderDelimitedTable resolveResult = new HeaderDelimitedTable(result.getTableStatus(),
					result.getDataTableType());
			return resolveResult;
		}
	}
}
