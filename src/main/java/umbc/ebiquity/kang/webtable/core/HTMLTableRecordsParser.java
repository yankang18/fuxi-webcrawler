package umbc.ebiquity.kang.webtable.core;

import static umbc.ebiquity.kang.webtable.core.HTMLTableTagDefinition.TABLE_DATA_TAG;
import static umbc.ebiquity.kang.webtable.core.HTMLTableTagDefinition.TABLE_HEADER_TAG;
import static umbc.ebiquity.kang.webtable.core.HTMLTableTagDefinition.TABLE_ROW_TAG;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.webtable.core.HTMLTableValidator.TableElementValidationResult;

/**
 * 
 * @author yankang
 *
 */
public class HTMLTableRecordsParser {

	/**
	 * Create horizontal table records from the specified
	 * {@link org.jsoup.nodes.Element}, which can be table, thead or tbody.
	 * 
	 * @param element
	 *            the Element from where the horizontal table records are
	 *            created
	 * @param rowStart
	 *            the start index of the row of the Element
	 * @param colBorderCount
	 *            the number of column each row has
	 * @return a <code>List</code> of <code>TableRecord</code>s
	 */
	public static List<TableRecord> createHorizontalTableRecords(Element element, int rowStart, int colBorderCount) {
		validateTableStructure(element);
		validateHorizontalParameters(element, rowStart, element.children().size() - 1, colBorderCount);
		return createTableRecordsFromHorizontalTableElement(element, rowStart, element.children().size() - 1, colBorderCount);
	}

	/**
	 * Create horizontal table records from the specified
	 * {@link org.jsoup.nodes.Element}, which can be table, thead or tbody.
	 * 
	 * @param element
	 *            the Element from where the horizontal table records are
	 *            created
	 * @param rowStart
	 *            the start index of the row of the Element
	 * @param rowEnd
	 *            the end index of the row of the Element
	 * @param colBorderCount
	 *            the number of column each row has
	 * @return a <code>List</code> of <code>TableRecord</code>s. Each
	 *         TableRecord contains the same number of cells.
	 */
	public static List<TableRecord> createTableRecordsFromHorizontalTableElement(Element element, int rowStart, int rowEnd,
			int colBorderCount) {
		validateTableStructure(element);
		validateHorizontalParameters(element, rowStart, rowEnd, colBorderCount);

		List<TableRecord> records = new ArrayList<TableRecord>(rowEnd + 1);
		String tagPath = element.tagName();
		Elements recElems = element.children();

		for (int i = rowStart; i <= rowEnd; i++) {
			Element row = recElems.get(i);
			if (TABLE_ROW_TAG.equals(row.tagName())) {
				TableRecord rec = new TableRecord(row, tagPath);
				rec.setSequenceNumber(i + 1);

				int colCount = 0;
				for (Element col : row.children()) {
					if (TABLE_DATA_TAG.equals(col.tagName()) || TABLE_HEADER_TAG.equals(col.tagName())) {
						TableCell tc = new TableCell(col, rec.getTagPath());
						rec.addData(tc);
						colCount++;
					}
				}

				// Fill up the missing columns in current row.
				int diff = colBorderCount - colCount;
				if (diff > 0) {
					List<TableCell> cells = rec.getTableCells();
					// Gets the last table cell from current table record and
					// use it to fill all the missing columns in current now.
					Element lastCell = cells.get(cells.size() - 1).getWrappedElement();
					for (int j = 0; j < diff; j++) {
						TableCell tc = new TableCell(lastCell, rec.getTagPath());
						rec.addData(tc);
					}
				}
				records.add(rec);
			}
		}
		return records;
	}

	/***
	 * Create vertical table records from the specified Element, which can be
	 * table, thead or tbody.
	 * 
	 * @param element
	 *            the Element from where the horizontal table records are
	 *            created
	 * @param rowStart
	 *            the start index of the row of the Element
	 * @param rowEnd
	 *            the end index of the row of the Element
	 * @param colBorderCount
	 *            the number of column each row has
	 * @return a <code>List</code> of <code>TableRecord</code>s
	 */
	public static List<TableRecord> createTableRecordsFromVeriticalTableElement(Element element, int rowStart, int rowEnd,
			int colBorderCount) {
		validateTableStructure(element);
		validateVerticalParameters(element, rowStart, rowEnd, colBorderCount);

		String tagPath = element.tagName();

		int rowCount = rowEnd - rowStart + 1;
		List<TableRecord> records = new ArrayList<TableRecord>(rowCount);
		for (int i = 0; i < rowCount; i++) {
			TableRecord rec = new TableRecord(tagPath);
			rec.setSequenceNumber(i + 1);
			records.add(rec);
		}

		Elements elements = element.children();
		for (int i = 0; i < colBorderCount; i++) {
			Element col = elements.get(i);
			if (TABLE_ROW_TAG.equals(col.tagName())) {

				int rowCounter = 0;
				for (Element row : col.children()) {
					if (TABLE_DATA_TAG.equals(row.tagName()) || TABLE_HEADER_TAG.equals(row.tagName())) {
						rowCounter++;

						// If current row does not reach the start row, skip
						// current now
						if (rowCounter - 1 < rowStart) {
							continue;
						}

						TableCell td = new TableCell(row, element.tagName());
						records.get(rowCounter - 1 - rowStart).addData(td);

						// If current row reaches the end row, the creation of
						// the table cell is completed
						if (rowCounter - 1 >= rowEnd)
							break;
					}
				}

				// Fill up the missing columns in current row
				int diff = rowEnd + 1 - rowCounter;
				if (diff > 0) {
					// Gets the last table cell from current table record and
					// use it to fill all the missing columns in current now.
					List<TableCell> cells = records.get(rowCounter - 1 - rowStart).getTableCells();
					Element lastCell = cells.get(cells.size() - 1).getWrappedElement();
					for (int j = 0; j < diff; j++) {
						rowCounter++;
						TableCell tc = new TableCell(lastCell, element.tagName());
						records.get(rowCounter - 1 - rowStart).addData(tc);
					}
				}
			}
		}
		return records;
	}

	private static void validateTableStructure(Element element) {
		TableElementValidationResult result = HTMLTableValidator.validateTableStructure(element);
		if (!result.isValid()) {
			throw new IllegalArgumentException(result.getMessage());
		}
	}

	private static void validateHorizontalParameters(Element element, int rowStart, int rowEnd, int colBorderCount) {

		if (colBorderCount < 1) {
			throw new IllegalArgumentException("Column border count should be at least one. ");
		}

		if (rowEnd + 1 > element.children().size() || rowStart + 1 > element.children().size() || rowStart > rowEnd) {
			throw new IllegalArgumentException(
					"Row start index and row end index should be in the range of number of rows in the Element, "
							+ "and row start index should be less than or equal to row end index.");
		}

	}

	private static void validateVerticalParameters(Element element, int rowStart, int rowEnd, int colBorderCount) {

		if (colBorderCount < 1) {
			throw new IllegalArgumentException("Column border count should be at least one. ");
		}

		if (colBorderCount > element.children().size()) {
			throw new IllegalArgumentException(
					"Column border count should be less than or equal to the number of child elements of the inputted element. ");
		}

		int MinVerticalColumnCount = HTMLTableRecordsCounter.getMinVerticalColumnCount(element);
		if (rowStart + 1 > MinVerticalColumnCount || rowStart > rowEnd) {
			throw new IllegalArgumentException(
					"Row start index should be in the range of minimal number of rows in the Element, "
							+ "and row start index should be less than or equal to row end index.");
		}
	}
}
