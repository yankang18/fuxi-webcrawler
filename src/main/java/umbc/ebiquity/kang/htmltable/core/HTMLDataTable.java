package umbc.ebiquity.kang.htmltable.core;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.htmldocument.util.BasicValidator;
import umbc.ebiquity.kang.htmltable.IHTMLDataTable;
import umbc.ebiquity.kang.htmltable.core.HTMLTableRecordsCounter.TableBorder;
import umbc.ebiquity.kang.htmltable.util.HTMLTableValidator;

/**
 * This class represents a POJO data table constructed from a HTML table
 * represented by {@link org.jsoup.nodes.Element}. The
 * <code>HTMLDataTable</code> can be either horizontal data table or vertical
 * data table.
 * <p>
 * The horizontal data table is defined as a table that each row is a record
 * while the vertical data table is defined as a table that each column is a
 * record
 * </p>
 * 
 * @author yankang
 *
 */
public class HTMLDataTable implements IHTMLDataTable {

	private List<TableRecord> records;
	private Element elem;
	private int rowCount;
	private int colCount;

	HTMLDataTable(Element element) {
		elem = element;
		records = new ArrayList<TableRecord>();
	}

	/**
	 * Convert a HTML table represented by {@link org.jsoup.nodes.Element} to a
	 * horizontal <code>HTMLDataTable</code>.
	 * 
	 * @param element
	 *            a HTML table represented by {@link org.jsoup.nodes.Element} to
	 *            be converted
	 * @return a HTMLDataTable
	 */
	public static HTMLDataTable convertToHorizontalDataTable(Element element) {
		HTMLTableValidator.isDataTable(element);

		TableBorder border = HTMLTableRecordsCounter.calculateTableBorder(element);
		int rowBorderCount = border.getHorizontalRecordCount();
		int colBorderCount = border.getVerticalRecordCount();
		if (colBorderCount == 0 || rowBorderCount == 0) {
			return null;
		}

		List<TableRecord> records = HTMLTableRecordsParser.createTableRecordsFromHorizontalTableElement(element, 0,
				rowBorderCount - 1, colBorderCount);

		HTMLDataTable dt = new HTMLDataTable(element);
		dt.setTableRecords(records);
		dt.setRowCount(rowBorderCount);
		dt.setColumnCount(colBorderCount); 
		return dt;
	}

	/**
	 * Convert a HTML table represented by {@link org.jsoup.nodes.Element} to a
	 * vertical <code>HTMLDataTable</code>.
	 * 
	 * @param element
	 *            a HTML table represented by {@link org.jsoup.nodes.Element} to
	 *            be converted
	 * @return a HTMLDataTable
	 */
	public static HTMLDataTable convertToVerticalDataTable(Element element) {
		HTMLTableValidator.isDataTable(element);
		
		TableBorder border = HTMLTableRecordsCounter.calculateTableBorder(element);
		int colBorderCount = border.getHorizontalRecordCount();
		int rowBorderCount = border.getVerticalRecordCount();
		if (colBorderCount == 0 || rowBorderCount == 0) {
			return null;
		}

		List<TableRecord> records = HTMLTableRecordsParser.createTableRecordsFromVeriticalTableElement(element, 0,
				rowBorderCount - 1, colBorderCount);

		HTMLDataTable dt = new HTMLDataTable(element);
		dt.setTableRecords(records);
		dt.setRowCount(rowBorderCount);
		dt.setColumnCount(colBorderCount);
		return dt;
	}

	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public int getColumnCount() {
		return colCount;
	}
	
	@Override
	public List<TableRecord> getTableRecords() {
		return records;
	}
	
	@Override
	public Element getWrappedElement(){
		return elem;
	}

	private void setTableRecords(List<TableRecord> records) {
		this.records = records;
	}

	private void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	private void setColumnCount(int columnCount) {
		this.colCount = columnCount;
	}

}
