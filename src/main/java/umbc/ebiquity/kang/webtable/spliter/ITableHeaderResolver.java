package umbc.ebiquity.kang.webtable.spliter;

public interface ITableHeaderResolver {
	
	public enum TableStatus {
		RegularTable, UnRegularTable
	}

	public enum DataTableHeaderType {
		IVT, /* Invalid Table */
		NHT, /* Non-header Table */
		VHT, /* Vertical Header Table */
		HHT, /* Horizontal Header Table */
		TDH, /* Two Directional Header Table */
		UD   /* UnDetermined */
	}
}
