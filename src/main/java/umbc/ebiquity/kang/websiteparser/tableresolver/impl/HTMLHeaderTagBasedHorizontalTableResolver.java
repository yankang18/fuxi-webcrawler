package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableRecordConstructor.createHorizontalTableRecord;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.TableStatus;

public class HTMLHeaderTagBasedHorizontalTableResolver extends AbstractHTMLHeaderTagBasedTableResolver  {

	private HTMLTableHeaderResolver headerResolver;

	public HTMLHeaderTagBasedHorizontalTableResolver() {
		headerResolver = new HTMLTableHeaderResolver();
	}
	
	@Override
	public TableResolveResult resolve(Element element) {
		System.out.println("resolveHorizontalHeaderTable");
		Elements theads = element.getElementsByTag("thead");
		if (theads.size() > 0) {
			Element thead = theads.get(0);
			Elements elements = thead.children();
			if (elements.size() > 0) {
				// TODO: if here the returned TableStatus is Regular and
				// DataTableType is UD, should we try
				// determineHorizontalHeaderFromBody?
				Elements tbodys = element.getElementsByTag("tbody");
				if (tbodys.size() <= 0) {
					return convertToTableResolveResult(null,
							new TableHeaderResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, false));
				}
				Element tbody = tbodys.get(0);
				return convertToTableHeaderResolveResult(tbody, thead, headerResolver.resolveHorizontalHeader(elements, true));
			} else {
				return resolveHeaderFromBody(element, new IHeaderResolutionWorker() {
					@Override
					public TableHeaderResolveResult resolveHeader(Elements elements) {
						return headerResolver.resolveHorizontalHeader(elements, false);
					}
				});
			}

		} else {
			// when there is no thead in the table
			return resolveHeaderFromBody(element, new IHeaderResolutionWorker() {
				@Override
				public TableHeaderResolveResult resolveHeader(Elements elements) {
					return headerResolver.resolveHorizontalHeader(elements, false);
				}
			});
		}
	}

	@Override
	protected TableResolveResult convertToTableResolveResult(Element element, TableHeaderResolveResult result) {
		
		if (DataTableHeaderType.HHT == result.getDataTableType()) {

			Position position = result.getHorizontalHeaderPosition();

			List<TableRecord> headerRecords = createHorizontalTableRecord(element, position.getRowStart(),
					position.getRowEnd(), position.getColumnBorderCount());
			List<TableRecord> dataRecords = createHorizontalTableRecord(element, position.getRowEnd() + 1,
					position.getRowBorderCount() - 1, position.getColumnBorderCount());

			TableResolveResult resolveResult = new TableResolveResult(result.getTableStatus(),
					result.getDataTableType());
			resolveResult.setHorizontalHeaderRecords(headerRecords);
			resolveResult.setHorizontalDataRecords(dataRecords);
			return resolveResult;

		} else {
			TableResolveResult resolveResult = new TableResolveResult(result.getTableStatus(),
					result.getDataTableType());
			return resolveResult;
		}
	}
	
	private TableResolveResult convertToTableHeaderResolveResult(Element tBody, Element thead,
			TableHeaderResolveResult result) {

		if (DataTableHeaderType.HHT == result.getDataTableType()) {
			Position position = result.getHorizontalHeaderPosition();

			List<TableRecord> headerRecords = createHorizontalTableRecord(thead, position.getRowStart(),
					position.getRowEnd(), position.getColumnBorderCount());
			List<TableRecord> dataRecords = createHorizontalTableRecord(tBody, 0, position.getColumnBorderCount());

			TableResolveResult resolveResult = new TableResolveResult(result.getTableStatus(),
					result.getDataTableType());
			resolveResult.setHorizontalHeaderRecords(headerRecords);
			resolveResult.setHorizontalDataRecords(dataRecords);
			return resolveResult;

		} else {
			TableResolveResult resolveResult = new TableResolveResult(result.getTableStatus(),
					result.getDataTableType());
			return resolveResult;
		}
	}

}
