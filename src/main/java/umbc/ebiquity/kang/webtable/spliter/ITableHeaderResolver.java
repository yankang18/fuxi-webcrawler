package umbc.ebiquity.kang.webtable.spliter;

public interface ITableHeaderResolver {
	
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
