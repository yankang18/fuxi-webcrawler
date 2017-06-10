package umbc.ebiquity.kang.htmltable;

import java.util.List;
import java.util.Set;

import umbc.ebiquity.kang.htmltable.core.DataCell;

public interface IDataCellHolder {

	DataCell getDataCell(String key);

	Set<String> getDataCellKeySet();

	List<DataCell> getDataCells(); 

}
