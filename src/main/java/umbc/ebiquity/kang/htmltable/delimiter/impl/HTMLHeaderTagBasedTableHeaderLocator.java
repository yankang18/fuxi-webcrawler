package umbc.ebiquity.kang.htmltable.delimiter.impl;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.htmltable.core.HTMLTableRecordsCounter;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;

/**
 * This class is to locate the position of the headers in a table. The locating
 * result is encapsulated in a <code>TableHeaderLocatingResult</code>
 * 
 * @author yankang
 *
 */
public class HTMLHeaderTagBasedTableHeaderLocator {
	
	/***
	 * Locates horizontal header location in the specified <code>Elements</code>
	 * 
	 * @param elements
	 *            a list of tr elements
	 * @param isTableHead
	 *            indicates that if the specified list of tr elements are
	 *            contained in a thead element. True, if yes. False, otherwise
	 * @return <code>TableHeaderResolveResult</code>
	 */
	public TableHeaderLocatingResult locateHorizontalHeader(Elements elements, boolean isTableHead) {
		if (elements.size() == 0) {
			return new TableHeaderLocatingResult(TableStatus.UnRegularTable, DataTableHeaderType.UnDetermined, isTableHead,
					TableHeaderLocatingResult.NO_CONTENT);
		}
		
		int numberOfRow = elements.size();
		Boolean[] header = new Boolean[numberOfRow];
		init(header);

		int minCol = Integer.MAX_VALUE;
		for (int index = 0; index < numberOfRow; index++) {
			Element row = elements.get(index);
			// Iterate each row that is in "tr" HTML tag.
			if (row.tagName().equalsIgnoreCase("tr")) {
				Elements cells = row.children();
				if (cells.size() > 0) {
					// Iterate each cell in a row to check if all cells in this
					// row are in "th" HTML tag.
					//
					// Or, if the input element is a "thead" element, indicated
					// by isTableHead boolean, check if all cells in this row
					// are in either "th" or "td" HTML tag.
					//
					// If false, this row is not a head. Otherwise, this row is
					// a head.
					for (Element cell : cells) {
						if (!cell.tagName().equalsIgnoreCase("th")
								&& (!isTableHead || !cell.tagName().equalsIgnoreCase("td"))) {
							header[index] = false;
							break;
						}
					}
					if (header[index]) {
						if (minCol > cells.size()) {
							minCol = cells.size();
						}
					}
				} else {
					header[index] = false;
				}
			} else {
				// not all rows are headers --> not regular
				return new TableHeaderLocatingResult(TableStatus.UnRegularTable, DataTableHeaderType.UnDetermined, isTableHead,
						TableHeaderLocatingResult.UNEXPECTED_TABLE_ROW);
			}
		}

		// no header exists.
		if (!hasHeaders(header)) {
			return new TableHeaderLocatingResult(TableStatus.UnRegularTable, DataTableHeaderType.UnDetermined, isTableHead,
 TableHeaderLocatingResult.NO_HEADER);
		}

		// # If the program reaches this point, it means that header exists. We
		// are going to find the first row that is not a header. Since this
		// row separates the headers and the data, we call this row the
		// Separator.
		int split = header.length;
		for (int index = 0; index < header.length; index++) {
			if (header[index] == false) {
				split = index;
				break;
			}
		}

		// To check if there are headers after the Separator. 
		boolean containsHeaderAfterSplit = false;
		for (int index = split + 1; index < header.length; index++) {
			if (header[index] == true) {
				containsHeaderAfterSplit = true;
				break;
			}
		}

		// If there still have headers after Separator, we consider the table is
		// irregular.
		if (containsHeaderAfterSplit)
			return new TableHeaderLocatingResult(TableStatus.UnRegularTable, DataTableHeaderType.UnDetermined,
					isTableHead, TableHeaderLocatingResult.UNEXPECTED_HEADER);

		// # If the program reaches this point, it means that headers are found
		// and valid, we are going return these information.
		TableHeaderLocatingResult result = new TableHeaderLocatingResult(TableStatus.RegularTable,
				DataTableHeaderType.HorizontalHeaderTable, isTableHead);
		result.setHorizontalHeaderPosition(new HeaderPosition(0, split - 1, numberOfRow, minCol));
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
	 * Locates vertical header location in the specified <code>Elements</code>
	 * 
	 * @param elements
	 *            a list of tr elements
	 * @return <code>TableHeaderResolveResult</code>
	 */
	public TableHeaderLocatingResult locateVeriticalHeader(Elements elements) {
		if (elements.size() == 0) {
			return new TableHeaderLocatingResult(TableStatus.UnRegularTable, DataTableHeaderType.UnDetermined, false,
					TableHeaderLocatingResult.NO_CONTENT);
		}

		boolean isVerticalHeader = true;

		// A simple implementation: only check whether the first column (i.e.,
		// first row if you see the table vertically) is header column. More
		// specifically, checking if all the cells in the first column are in
		// "th" HTML tag. If yes, we consider the first column is a header
		// column.
		for (int index = 0; index < elements.size(); index++) {
			Element row = elements.get(index);
			if (row.tagName().equalsIgnoreCase("tr")) {
				Elements cells = row.children();
				if (cells.size() <= 0 || !cells.get(0).tagName().equalsIgnoreCase("th")) {
					isVerticalHeader = false;
					break;
				}
			} else {
				// not all rows are headers --> not regular
				return new TableHeaderLocatingResult(TableStatus.UnRegularTable, DataTableHeaderType.UnDetermined, false,
						TableHeaderLocatingResult.UNEXPECTED_TABLE_ROW);
			}
		}

		TableHeaderLocatingResult result = null;
		if (isVerticalHeader) {
			int rowBorderCount = HTMLTableRecordsCounter.getMinVerticalColumnCount(elements);
			result = new TableHeaderLocatingResult(TableStatus.RegularTable, DataTableHeaderType.VerticalHeaderTable, false);
			// 0, 0 indicates that the header rows starting at index 0 and
			// ending at index 0 which means that the first row is the header
			// row.
			HeaderPosition position = new HeaderPosition(0, 0, rowBorderCount, elements.size());
			result.setVerticalHeaderPosition(position);
		} else {
			result = new TableHeaderLocatingResult(TableStatus.RegularTable, DataTableHeaderType.UnDetermined, false,
					TableHeaderLocatingResult.NO_HEADER);
		}
		return result;
	}
	
	private void init(Boolean[] header) {
		for (int i = 0; i < header.length; i++) {
			header[i] = true;
		}
	}
}
