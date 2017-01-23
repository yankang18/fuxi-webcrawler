package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

public class Position {

	private int rowStart;
	private int rowEnd;
	private int rowBorderCount;
	private int colBorderCount;

	/**
	 * 
	 * @param rowStart
	 * @param rowEnd
	 * @param rowBorderCount
	 * @param columnBorderCount
	 */
	public Position(int rowStart, int rowEnd, int rowBorderCount, int columnBorderCount) {
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
