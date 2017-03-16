package umbc.ebiquity.kang.webtable.spliter.impl;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.webtable.spliter.AbstractHTMLHeaderTagBasedTableSpliter;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;

public class HTMLHeaderTagBasedHorizontalTableSpliter extends AbstractHTMLHeaderTagBasedTableSpliter  {

	private HTMLHeaderTagBasedTableHeaderLocator headersLocator;

	public HTMLHeaderTagBasedHorizontalTableSpliter() {
		headersLocator = new HTMLHeaderTagBasedTableHeaderLocator();
	}
	
	@Override
	public TableSplitingResult split(Element element) {
		HTMLTableValidator.validateTableElement(element);
		
		Elements theads = element.getElementsByTag("thead");
		if (theads.size() > 0) {
			// In the scenario that there is a thead in the table
			Element thead = theads.get(0);
			Elements theadContent = thead.children();
			if (theadContent.size() > 0) {
				// TODO: if here the returned TableStatus is Regular and
				// DataTableType is UD, should we try
				// determineHorizontalHeaderFromBody?
				Elements tbodys = element.getElementsByTag("tbody");
				if (tbodys.size() <= 0) {
					return convertToTableResolveResult(null,
							new TableHeaderLocatingResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, false));
				}
				Element tbody = tbodys.get(0);
				return convertToTableHeaderResolveResult(tbody, thead, headersLocator.locateHorizontalHeader(theadContent, true));
			} else {
				return resolveHeaderFromBody(element, new IHeaderLocatingBorker() {
					@Override
					public TableHeaderLocatingResult locateHeader(Elements elements) {
						return headersLocator.locateHorizontalHeader(elements, false);
					}
				});
			}

		} else {
			// In the scenario that there is no thead in the table
			return resolveHeaderFromBody(element, new IHeaderLocatingBorker() {
				@Override
				public TableHeaderLocatingResult locateHeader(Elements elements) {
					return headersLocator.locateHorizontalHeader(elements, false);
				}
			});
		}
	}

	@Override
	protected TableSplitingResult convertToTableResolveResult(Element element, TableHeaderLocatingResult result) {
		
		if (DataTableHeaderType.HHT == result.getDataTableType()) {

			HeaderPosition position = result.getHorizontalHeaderPosition();

			List<TableRecord> headerRecords = HTMLTableRecordsCreator.createHorizontalTableRecords(element,
					position.getRowStart(), position.getRowEnd(), position.getColumnBorderCount());
			List<TableRecord> dataRecords = HTMLTableRecordsCreator.createHorizontalTableRecords(element,
					position.getRowEnd() + 1, position.getRowBorderCount() - 1, position.getColumnBorderCount());

			TableSplitingResult resolveResult = new TableSplitingResult(result.getTableStatus(),
					result.getDataTableType());
			resolveResult.setHorizontalHeaderRecords(headerRecords);
			resolveResult.setHorizontalDataRecords(dataRecords);
			return resolveResult;

		} else {
			TableSplitingResult resolveResult = new TableSplitingResult(result.getTableStatus(),
					result.getDataTableType());
			return resolveResult;
		}
	}
	
	private TableSplitingResult convertToTableHeaderResolveResult(Element tBody, Element thead,
			TableHeaderLocatingResult result) {

		if (DataTableHeaderType.HHT == result.getDataTableType()) {
			HeaderPosition position = result.getHorizontalHeaderPosition();

			List<TableRecord> headerRecords = HTMLTableRecordsCreator.createHorizontalTableRecords(thead, position.getRowStart(),
					position.getRowEnd(), position.getColumnBorderCount());
			List<TableRecord> dataRecords = HTMLTableRecordsCreator.createHorizontalTableRecords(tBody, 0, position.getColumnBorderCount());

			TableSplitingResult resolveResult = new TableSplitingResult(result.getTableStatus(),
					result.getDataTableType());
			resolveResult.setHorizontalHeaderRecords(headerRecords);
			resolveResult.setHorizontalDataRecords(dataRecords);
			return resolveResult;

		} else {
			TableSplitingResult resolveResult = new TableSplitingResult(result.getTableStatus(),
					result.getDataTableType());
			return resolveResult;
		}
	}

}
