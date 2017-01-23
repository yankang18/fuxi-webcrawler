package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableTagDefinition.TABLE_BODY_TAG;
import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableTagDefinition.TABLE_DATA_TAG;
import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableTagDefinition.TABLE_HEADER_SECTION_TAG;
import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableTagDefinition.TABLE_HEADER_TAG;
import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableTagDefinition.TABLE_ROW_TAG;
import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableTagDefinition.TABLE_TAG;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLTableRecordConstructor {
	
	/**
	 * Create horizontal table records from the specified Element, which can be
	 * table, thead or tbody.
	 * 
	 * @param element
	 *            the Element from where the horizontal table records are
	 *            created
	 * @param rowStart
	 *            the start index of the row of the Element
	 * @param colBorderCount
	 *            the end index of the column of the Element
	 * @return a <code>List</code> of <code>TableRecord</code>s
	 */
	public static List<TableRecord> createHorizontalTableRecord(Element element, int rowStart, int colBorderCount) {
		validateTableElement(element);
		validateHorizontalParameters(element, rowStart, element.children().size() - 1, colBorderCount);
		return createHorizontalTableRecord(element, rowStart, element.children().size() - 1, colBorderCount);
	}

	/**
	 * Create horizontal table records from the specified Element, which can be
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
	 *            the end index of the column of the Element
	 * @return a <code>List</code> of <code>TableRecord</code>s
	 */
	public static List<TableRecord> createHorizontalTableRecord(Element element, int rowStart, int rowEnd,
			int colBorderCount) {

		validateTableElement(element);
		validateHorizontalParameters(element, rowStart, rowEnd, colBorderCount);

		List<TableRecord> records = new ArrayList<TableRecord>(rowEnd + 1);
		String tagPath = element.tagName();
		Elements recElems = element.children();

		for (int i = rowStart; i <= rowEnd; i++) {
			Element record = recElems.get(i);
			if (TABLE_ROW_TAG.equals(record.tagName())) {
				TableRecord rec = new TableRecord(record, tagPath);
				rec.setSequenceNumber(i + 1);

				int colCount = 0;
				for (Element cell : record.children()) {
					if (TABLE_DATA_TAG.equals(cell.tagName()) || TABLE_HEADER_TAG.equals(cell.tagName())) {
						TableCell tc = new TableCell(cell, rec.getTagPath());
						rec.addData(tc);
						colCount++;
					}
				}

				int diff = colBorderCount - colCount;
				if (diff > 0) {
					List<TableCell> cells = rec.getTableCells();
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
	 *            the end index of the column of the Element
	 * @return a <code>List</code> of <code>TableRecord</code>s
	 */
	public static List<TableRecord> createVerticalTableRecords(Element element, int rowStart, int rowEnd,
			int colBorderCount) {
		validateTableElement(element);
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
			Element child = elements.get(i);
			if (TABLE_ROW_TAG.equals(child.tagName())) {

				int rowCounter = 0;
				for (Element cell : child.children()) {
					if (TABLE_DATA_TAG.equals(cell.tagName()) || TABLE_HEADER_TAG.equals(cell.tagName())) {
						rowCounter++;

						if (rowCounter - 1 < rowStart) {
							continue;
						}

						TableCell td = new TableCell(cell, element.tagName());
						records.get(rowCounter - 1 - rowStart).addData(td);

						if (rowCounter - 1 >= rowEnd)
							break;
					}
				}

				int diff = rowEnd + 1 - rowCounter;
				if (diff > 0) {
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

	private static void validateTableElement(Element element) {
		
		if (!element.tagName().equalsIgnoreCase(TABLE_TAG) && !element.tagName().equalsIgnoreCase(TABLE_BODY_TAG)
				&& !element.tagName().equalsIgnoreCase(TABLE_HEADER_SECTION_TAG)) {
			throw new IllegalArgumentException(
					"Element should be " + TABLE_TAG + ", " + TABLE_BODY_TAG + ", or " + TABLE_HEADER_SECTION_TAG);
		}

		if (element.children().size() <= 0) {
			throw new IllegalArgumentException("Element should have content.");
		}

		for (Element e : element.children()) {
			if (!e.tagName().equalsIgnoreCase(TABLE_ROW_TAG)) {
				throw new IllegalArgumentException("Element should only contain " + TABLE_ROW_TAG + " elements");
			}

			if (e.children().size() <= 0) {
				throw new IllegalArgumentException(TABLE_ROW_TAG + " element should have content.");
			}
			
			for (Element c : e.children()) {
				if (!TABLE_DATA_TAG.equals(c.tagName()) && !TABLE_HEADER_TAG.equals(c.tagName())) {
					throw new IllegalArgumentException(TABLE_ROW_TAG + " elements should only contain either "
							+ TABLE_DATA_TAG + " or " + TABLE_HEADER_TAG);
				}
			}
		}
	}

	private static void validateHorizontalParameters(Element element, int rowStart, int rowEnd, int colBorderCount) {

		if (colBorderCount < 1) {
			throw new IllegalArgumentException("Column border count should be at least one. ");
		}

		if (rowEnd + 1 > element.children().size() || rowStart + 1 > element.children().size() || rowStart > rowEnd) {
			throw new IllegalArgumentException(
					"Row start index and row end index should be in the range of number of rows in the Element, " +
					"and row start index should be less than or equal to row end index.");
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

		int MinVerticalColumnCount = HTMLTableResolutionHelper.getMinVerticalColumnCount(element);
		if (rowStart + 1 > MinVerticalColumnCount || rowStart > rowEnd) {
			throw new IllegalArgumentException(
					"Row start index should be in the range of minimal number of rows in the Element, "
							+ "and row start index should be less than or equal to row end index.");
		}
	}
}
