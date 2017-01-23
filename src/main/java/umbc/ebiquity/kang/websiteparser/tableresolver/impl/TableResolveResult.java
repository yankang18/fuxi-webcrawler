package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import java.util.List;

import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver;

public class TableResolveResult implements ITableHeaderResolver {

	private TableStatus status;
	private DataTableHeaderType headerType;
	private List<TableRecord> verticalHeaderRecords;
	private List<TableRecord> verticalDataRecords;
	private List<TableRecord> horizontalDataRecords;
	private List<TableRecord> horizontalHeaderRecords;  

	public TableResolveResult(TableStatus tableStatus, DataTableHeaderType tableType) {
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
}
