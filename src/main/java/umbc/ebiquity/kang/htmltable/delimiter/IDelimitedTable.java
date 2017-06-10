package umbc.ebiquity.kang.htmltable.delimiter;

public interface IDelimitedTable {
	
	public enum TableStatus {
		RegularTable, UnRegularTable
	}

	public enum DataTableHeaderType {
		InvalidTable, /* Invalid Table */
		NonHeaderTable, /* Non-header Table */
		VerticalHeaderTable, /* Vertical Header Table */
		HorizontalHeaderTable, /* Horizontal Header Table */
		TwoDirectionalHeaderTable, /* Two Directional Header Table */
		UnDetermined   /* UnDetermined */
	}
}
