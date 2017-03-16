package umbc.ebiquity.kang.webtable.spliter.impl;

import java.util.List;

import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;

public class TableSplitingResult implements ITableHeaderResolver {

	private TableStatus status;
	private DataTableHeaderType headerType;
	private List<TableRecord> verticalHeaderRecords;
	private List<TableRecord> verticalDataRecords;
	private List<TableRecord> horizontalDataRecords;
	private List<TableRecord> horizontalHeaderRecords;  

	public TableSplitingResult(TableStatus tableStatus, DataTableHeaderType tableType) {
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
