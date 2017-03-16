package umbc.ebiquity.kang.webtable.spliter.impl;

import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver;

public class TableHeaderLocatingResult implements ITableHeaderResolver {

	private TableStatus status;
	private DataTableHeaderType type;
	private HeaderPosition vPosition;
	private HeaderPosition hPosition; 
	private boolean resolvedFromHeader;
	private String message;

	public static final String NO_HEADER = "Header does not exist";
	public static final String UNEXPECTED_HEADER = "Headers occur unexpected";
	public static final String UNEXPECTED_TABLE_ROW = "Not all children in tbody are tr";
	public static final String NO_CONTENT = "No Content"; 

	public TableHeaderLocatingResult(TableStatus tableStatus, DataTableHeaderType tableType, boolean resolvedFromHeader) { 
		status = tableStatus;
		type = tableType;
		this.resolvedFromHeader = resolvedFromHeader;
	}

	public TableHeaderLocatingResult(TableStatus tableStatus, DataTableHeaderType tableType, boolean resolvedFromHeader,
			String message) {
		this(tableStatus, tableType, resolvedFromHeader);
		this.message = message;
	}

	public void setVerticalHeaderPosition(HeaderPosition position) {
		this.vPosition = position;
	}
	
	public void setHorizontalHeaderPosition(HeaderPosition position) {
		this.hPosition = position;
	}

	/**
	 * @return the TableStatus
	 */
	public TableStatus getTableStatus() { 
		return status;
	}
	
	/**
	 * 
	 * @return the DataTableType
	 */
	public DataTableHeaderType getDataTableType() { 
		return type;
	}

	/**
	 * 
	 * @return Position representing the Vertical Header Position
	 */
	public HeaderPosition getVerticalHeaderPosition() {
		return vPosition;
	}

	/**
	 * 
	 * @return Position representing the Horizontal Header Position
	 */
	public HeaderPosition getHorizontalHeaderPosition() {
		return hPosition;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the resolvedFromHeader
	 */
	public boolean isResolvedFromHeader() {
		return resolvedFromHeader;
	}

}
