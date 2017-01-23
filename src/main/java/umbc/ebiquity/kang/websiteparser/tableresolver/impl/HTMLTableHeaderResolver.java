package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.TableStatus;

public class HTMLTableHeaderResolver {
	
	/***
	 * 
	 * @param elements a list of tr elements
	 * @param isTableHead is the list of tr elements contained in a thead element
	 * @return <code>TableHeaderResolveResult</code>
	 */
	public TableHeaderResolveResult resolveHorizontalHeader(Elements elements, boolean isTableHead) {
		if (elements.size() == 0) {
			return new TableHeaderResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, isTableHead,
					TableHeaderResolveResult.NO_CONTENT);
		}
		
		int numberOfRow = elements.size();
		Boolean[] header = new Boolean[numberOfRow];
		init(header);

		int maxCol = 0;
		for (int index = 0; index < numberOfRow; index++) {
			Element row = elements.get(index);
			if (row.tagName().equalsIgnoreCase("tr")) {
				Elements cells = row.children();
				if (cells.size() > 0) {
					for (Element cell : cells) {
						if (!cell.tagName().equalsIgnoreCase("th")
								&& (!isTableHead || !cell.tagName().equalsIgnoreCase("td"))) {
							header[index] = false;
							break;
						}
					}
					if (header[index]) {
						if (maxCol < cells.size()) {
							maxCol = cells.size();
						}
					}
				} else {
					header[index] = false;
				}
			} else {
				// not all children in tbody are tr --> not regular
				return new TableHeaderResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, isTableHead,
						TableHeaderResolveResult.UNEXPECTED_TABLE_ROW);
			}
		}

		// no header exists.
		if (!hasHeaders(header)) {
			return new TableHeaderResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, isTableHead,
					TableHeaderResolveResult.NO_HEADER);
		}

		// If the program reaches here, it means that header exists. 

		// We are going to find the border that separate the headers and
		// the data.
		int split = header.length;
		for (int index = 0; index < header.length; index++) {
			if (header[index] == false) {
				split = index;
				break;
			}
		}

		// To check if there are headers after the separator. 
		boolean containsHeaderAfterSplit = false;
		for (int index = split + 1; index < header.length; index++) {
			if (header[index] == true) {
				containsHeaderAfterSplit = true;
				break;
			}
		}

		// If there still have headers after separator, we consider the table is
		// irregular.
		if (containsHeaderAfterSplit)
			return new TableHeaderResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, isTableHead,
					TableHeaderResolveResult.UNEXPECTED_HEADER);

		TableHeaderResolveResult result = new TableHeaderResolveResult(TableStatus.RegularTable,
				DataTableHeaderType.HHT, isTableHead);
		result.setHorizontalHeaderPosition(new Position(0, split - 1, numberOfRow, maxCol));
		return result;
	}

	private boolean hasHeaders(Boolean[] header) {
		for (Boolean hasHeader : header) {
			if (hasHeader)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param elements
	 *            a list of tr elements
	 * @return
	 */
	public TableHeaderResolveResult resolveVeriticalHeader(Elements elements) {
		if (elements.size() == 0) {
			return new TableHeaderResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, false,
					TableHeaderResolveResult.NO_CONTENT);
		}

		boolean isVerticalHeader = true;
		for (int index = 0; index < elements.size(); index++) {
			Element row = elements.get(index);
			if (row.tagName().equalsIgnoreCase("tr")) {
				System.out.println("row_" + index);
				Elements cells = row.children();
				if (cells.size() <= 0 || !cells.get(0).tagName().equalsIgnoreCase("th")) {
					isVerticalHeader = false;
					break;
				}
			} else {
				// not all children in tbody are tr, not regular
				return new TableHeaderResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, false,
						TableHeaderResolveResult.UNEXPECTED_TABLE_ROW);
			}
		}

		TableHeaderResolveResult result = null;
		if (isVerticalHeader) {
			System.out.println("is Vertical Header");
			int rowBorderCount = HTMLTableResolutionHelper.getMaxVerticalColumnCount(elements);
			result = new TableHeaderResolveResult(TableStatus.RegularTable, DataTableHeaderType.VHT, false);
			Position position = new Position(0, 0, rowBorderCount, elements.size());
			result.setVerticalHeaderPosition(position);
		} else {
			System.out.println("is Not Vertical Header");
			result = new TableHeaderResolveResult(TableStatus.RegularTable, DataTableHeaderType.UD, false,
					TableHeaderResolveResult.NO_HEADER);
		}
		return result;
	}
	
	private void init(Boolean[] header) {
		for (int i = 0; i < header.length; i++) {
			header[i] = true;
		}
	}
}
