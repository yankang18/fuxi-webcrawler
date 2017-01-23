package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.websiteparser.tableresolver.ITableResolver;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.TableStatus;
import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableRecordConstructor.createHorizontalTableRecord;
import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableRecordConstructor.createVerticalTableRecords;

public class HTMLHeaderTagBasedTableResolver implements ITableResolver {

//	private HTMLTableHeaderResolver headerResolver;
	private ITableResolver verticalTableResolver;
	private ITableResolver horizontalTableResolver;

	public HTMLHeaderTagBasedTableResolver() {
//		headerResolver = new HTMLTableHeaderResolver();
		verticalTableResolver = new HTMLHeaderTagBasedVerticalTableResolver();
		horizontalTableResolver = new HTMLHeaderTagBasedHorizontalTableResolver();
	}

	@Override
	public TableResolveResult resolve(Element element) {

//		TableResolveResult result1 = resolveVerticalHeaderTable(element);
//		TableResolveResult result2 = resolveHorizontalHeaderTable(element);
		TableResolveResult result1 = verticalTableResolver.resolve(element);
		TableResolveResult result2 = horizontalTableResolver.resolve(element);

		if (result1.getTableStatus() == TableStatus.UnRegularTable
				&& result2.getTableStatus() == TableStatus.UnRegularTable) {
			return new TableResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD);
		}

		System.out.println("Point 1");
		if (result1.getDataTableHeaderType() == DataTableHeaderType.VHT && result2.getDataTableHeaderType() == DataTableHeaderType.HHT) {
			TableResolveResult result = new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.TDH);
			result.setVerticalHeaderRecords(result1.getVerticalHeaderRecords());
			result.setVerticalDataRecords(result1.getVerticalDataRecords());
			result.setHorizontalHeaderRecords(result2.getHorizontalHeaderRecords());
			result.setHorizontalDataRecords(result2.getHorizontalDataRecords());
			System.out.println("Point 2");
			return result;
		} else if (result1.getDataTableHeaderType() == DataTableHeaderType.VHT) {
			System.out.println("Point 3");
			return result1;
		} else if (result2.getDataTableHeaderType() == DataTableHeaderType.HHT) {
			System.out.println("Point 4");
			return result2;
		} else {
			return new TableResolveResult(TableStatus.RegularTable, DataTableHeaderType.UD); 
		}
	}

//	/***
//	 * 
//	 * @param element table element
//	 * @return
//	 */
//	private TableResolveResult resolveHorizontalHeaderTable(Element element) { 
//		System.out.println("resolveHorizontalHeaderTable");
//		Elements theads = element.getElementsByTag("thead");
//		if (theads.size() > 0) {
//			Element thead = theads.get(0);
//			Elements elements = thead.children();
//			if (elements.size() > 0) {
//				// TODO: if here the returned TableStatus is Regular and
//				// DataTableType is UD, should we try
//				// determineHorizontalHeaderFromBody?
//				Elements tbodys = element.getElementsByTag("tbody");
//				if (tbodys.size() <= 0) {
//					return convertToTableResolveResult(null,
//							new TableHeaderResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, false));
//				}
//				Element tbody = tbodys.get(0);
//				return convertToTableHeaderResolveResult(tbody, thead, headerResolver.resolveHorizontalHeader(elements, true));
//			} else {
//				return resolveHeaderFromBody(element, new IHeaderResolutionWorker() {
//					@Override
//					public TableHeaderResolveResult resolveHeader(Elements elements) {
//						return headerResolver.resolveHorizontalHeader(elements, false);
//					}
//				});
//			}
//
//		} else {
//			// when there is no thead in the table
//			return resolveHeaderFromBody(element, new IHeaderResolutionWorker() {
//				@Override
//				public TableHeaderResolveResult resolveHeader(Elements elements) {
//					return headerResolver.resolveHorizontalHeader(elements, false);
//				}
//			});
//		}
//	}
	
//	private TableResolveResult resolveVerticalHeaderTable(Element element) {
//		System.out.println("resolveVerticalHeaderTable");
//		return resolveHeaderFromBody(element, new IHeaderResolutionWorker() {
//			@Override
//			public TableHeaderResolveResult resolveHeader(Elements elements) {
//				return headerResolver.resolveVeriticalHeader(elements);
//			}
//		});
//	}
	
//	private TableResolveResult resolveHeaderFromBody(Element tableElem, IHeaderResolutionWorker worker){
//		Elements tbodies = tableElem.getElementsByTag("tbody");
//		if (tbodies.size() > 0) {
//			Element tBody = tbodies.get(0);
//			Elements elements = tBody.children();
//			if (elements.size() > 0) {
//				return convertToTableResolveResult(tBody, worker.resolveHeader(elements)); 
//			} else {
//				// no row in tbody --> not regular
//				return convertToTableResolveResult(tBody,
//						new TableHeaderResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, false));
//			}
//		} else {
//			// there is no tbody 
//			Elements elements = tableElem.children();
//			if (elements.size() > 0) {
//				return convertToTableResolveResult(tableElem, worker.resolveHeader(elements)); 
//			} else {
//				// no row in table --> not regular
//				return convertToTableResolveResult(tableElem,
//						new TableHeaderResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, false));
//			}
//		}
//	}
//	
//	private TableResolveResult convertToTableHeaderResolveResult(Element tBody, Element thead, TableHeaderResolveResult result) {
//		
//		if (DataTableHeaderType.HHT == result.getDataTableType()) {
//			Position position = result.getHorizontalHeaderPosition();
//			TableResolveResult resolveResult = new TableResolveResult(result.getTableStatus(),
//					result.getDataTableType());
//
//			List<TableRecord> headerRecords = createHorizontalTableRecord(thead, position.getRowStart(),
//					position.getRowEnd(), position.getColumnBorderCount());
//			List<TableRecord> dataRecords = createHorizontalTableRecord(tBody, 0, position.getColumnBorderCount());
//
//			resolveResult.setHorizontalHeaderRecords(headerRecords);
//			resolveResult.setHorizontalDataRecords(dataRecords);
//			return resolveResult;
//
//			
//		} else {
//			TableResolveResult resolveResult = new TableResolveResult(result.getTableStatus(),
//					result.getDataTableType());
//			return resolveResult;
//		}
//	}
	
//	private TableResolveResult convertToTableResolveResult(Element element, TableHeaderResolveResult result) {
//
//		if (DataTableHeaderType.HHT == result.getDataTableType()) {
//			
//			Position position = result.getHorizontalHeaderPosition();
//
//			List<TableRecord> headerRecords = createHorizontalTableRecord(element, position.getRowStart(),
//					position.getRowEnd(), position.getColumnBorderCount());
//			List<TableRecord> dataRecords = createHorizontalTableRecord(element, position.getRowEnd() + 1,
//					position.getRowBorderCount() - 1, position.getColumnBorderCount());
//
//			TableResolveResult resolveResult = new TableResolveResult(result.getTableStatus(),
//					result.getDataTableType());
//			resolveResult.setHorizontalHeaderRecords(headerRecords);
//			resolveResult.setHorizontalDataRecords(dataRecords);
//			return resolveResult;
//			
//		} else if (DataTableHeaderType.VHT == result.getDataTableType()) {
//			
//			Position position = result.getVerticalHeaderPosition();
//			
//			List<TableRecord> headerRecords = createVerticalTableRecords(element, position.getRowStart(),
//					position.getRowEnd(), position.getColumnBorderCount());
//			List<TableRecord> dataRecords = createVerticalTableRecords(element, position.getRowEnd() + 1,
//					position.getRowBorderCount() - 1, position.getColumnBorderCount());
//
//			TableResolveResult resolveResult = new TableResolveResult(result.getTableStatus(),
//					result.getDataTableType());
//			resolveResult.setVerticalHeaderRecords(headerRecords);
//			resolveResult.setVerticalDataRecords(dataRecords);
//			return resolveResult;
//			
//		} else {
//			TableResolveResult resolveResult = new TableResolveResult(result.getTableStatus(),
//					result.getDataTableType());
//			return resolveResult;
//		}
//	}
//
//	private interface IHeaderResolutionWorker {
//		TableHeaderResolveResult resolveHeader(Elements elements);
//	}

}
