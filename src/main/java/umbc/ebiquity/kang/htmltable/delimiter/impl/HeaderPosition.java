package umbc.ebiquity.kang.htmltable.delimiter.impl;

public class HeaderPosition {

	private int rowStart;
	private int rowEnd;
	private int rowBorderCount;
	private int colBorderCount;

	/**
	 * 
	 * @param rowStart
	 *            the index of the start row that is a header row
	 * @param rowEnd
	 *            the index of the end row that is a header row
	 * @param rowBorderCount
	 *            the number of rows
	 * @param columnBorderCount
	 *            the number of columns
	 */
	public HeaderPosition(int rowStart, int rowEnd, int rowBorderCount, int columnBorderCount) {
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
		this.rowBorderCount = rowBorderCount;
		this.colBorderCount = columnBorderCount;
	}

	/**
	 * @return the rowStart
	 */
	public int getRowStart() {
		return rowStart;
	}

	/**
	 * @return the rowEnd
	 */
	public int getRowEnd() {
		return rowEnd;
	}

	/**
	 * 
	 * @return
	 */
	public int getRowBorderCount() {
		return rowBorderCount;
	}

	/**
	 * 
	 * @return
	 */
	public int getColumnBorderCount() {
		return colBorderCount;
	}

}
