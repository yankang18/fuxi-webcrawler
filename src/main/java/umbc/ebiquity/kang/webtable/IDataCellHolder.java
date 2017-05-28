package umbc.ebiquity.kang.webtable;

import java.util.List;
import java.util.Set;

import umbc.ebiquity.kang.webtable.core.DataCell;

public interface IDataCellHolder {

	DataCell getDataCell(String key);

	Set<String> getDataCellKeySet();

	List<DataCell> getDataCells(); 

}
