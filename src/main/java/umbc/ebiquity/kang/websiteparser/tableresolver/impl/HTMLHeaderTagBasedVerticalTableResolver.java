package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableRecordConstructor.createVerticalTableRecords;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.DataTableHeaderType;

public class HTMLHeaderTagBasedVerticalTableResolver extends AbstractHTMLHeaderTagBasedTableResolver {

	private HTMLTableHeaderResolver headerResolver;

	public HTMLHeaderTagBasedVerticalTableResolver() {
		headerResolver = new HTMLTableHeaderResolver();
	}
	
	@Override
	public TableResolveResult resolve(Element element) {
		System.out.println("resolveVerticalHeaderTable");
		return resolveHeaderFromBody(element, new IHeaderResolutionWorker() {
			@Override
			public TableHeaderResolveResult resolveHeader(Elements elements) {
				return headerResolver.resolveVeriticalHeader(elements);
			}
		});
	}

	@Override
	protected TableResolveResult convertToTableResolveResult(Element element, TableHeaderResolveResult result) {
		
		if (DataTableHeaderType.VHT == result.getDataTableType()) {

			Position position = result.getVerticalHeaderPosition();

			List<TableRecord> headerRecords = createVerticalTableRecords(element, position.getRowStart(),
					position.getRowEnd(), position.getColumnBorderCount());
			List<TableRecord> dataRecords = createVerticalTableRecords(element, position.getRowEnd() + 1,
					position.getRowBorderCount() - 1, position.getColumnBorderCount());

			TableResolveResult resolveResult = new TableResolveResult(result.getTableStatus(),
					result.getDataTableType());
			resolveResult.setVerticalHeaderRecords(headerRecords);
			resolveResult.setVerticalDataRecords(dataRecords);
			return resolveResult;

		} else {
			TableResolveResult resolveResult = new TableResolveResult(result.getTableStatus(),
					result.getDataTableType());
			return resolveResult;
		}
	}
}
