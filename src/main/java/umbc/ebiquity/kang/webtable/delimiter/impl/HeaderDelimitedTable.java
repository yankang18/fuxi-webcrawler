package umbc.ebiquity.kang.webtable.delimiter.impl;

import java.util.List;

import umbc.ebiquity.kang.htmldocument.IHtmlElement;
import umbc.ebiquity.kang.htmldocument.impl.StandardHtmlElement;
import umbc.ebiquity.kang.webtable.core.TableRecord;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.TableStatus;

public class HeaderDelimitedTable implements IDelimitedTable {

	private IHtmlElement htmlElement;
	private TableStatus status;
	private DataTableHeaderType headerType;
	private List<TableRecord> verticalHeaderRecords;
	private List<TableRecord> verticalDataRecords;
	private List<TableRecord> horizontalDataRecords;
	private List<TableRecord> horizontalHeaderRecords;  

	public HeaderDelimitedTable(TableStatus tableStatus, DataTableHeaderType tableType) {
		status = tableStatus;
		headerType = tableType;
	}

	public DataTableHeaderType getDataTableHeaderType() {
		return headerType;
	}

	public TableStatus getTableStatus() {
		return status;
	}

	public void setVerticalHeaderRecords(List<TableRecord> headerRecords) {
		this.verticalHeaderRecords = headerRecords;
	}

	public void setVerticalDataRecords(List<TableRecord> dataRecords) {
		this.verticalDataRecords = dataRecords;
	}

	public List<TableRecord> getVerticalHeaderRecords() {
		return this.verticalHeaderRecords;
	}

	public List<TableRecord> getVerticalDataRecords() {
		return this.verticalDataRecords; 
	}

	public void setHorizontalHeaderRecords(List<TableRecord> headerRecords) {
		this.horizontalHeaderRecords = headerRecords;
	}

	public void setHorizontalDataRecords(List<TableRecord> dataRecords) {
		this.horizontalDataRecords = dataRecords;
	}

	public List<TableRecord> getHorizontalHeaderRecords() {
		return horizontalHeaderRecords;
	}

	public List<TableRecord> getHorizontalDataRecords() {
		return horizontalDataRecords;
	}

	public void setHtmlElement(IHtmlElement htmlElement) {
		this.htmlElement = htmlElement;
	}

	public IHtmlElement getHTMLTableElement() {
		return htmlElement;
	}
}