package umbc.ebiquity.kang.htmltable.delimiter.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmltable.delimiter.ITableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.htmltable.util.HTMLTableValidator;

public class StandardTableHeaderDelimiter implements ITableHeaderDelimiter {

	List<ITableHeaderDelimiter> delimiters;

	public StandardTableHeaderDelimiter() {
		delimiters = new ArrayList<>(2);
		delimiters.add(new HTMLHeaderTagBasedTableHeaderDelimiter());
		delimiters.add(new ClusteringBasedTableHeaderDelimiter());
	}

	@Override
	public HeaderDelimitedTable delimit(Element element) {
		HTMLTableValidator.isTable(element);

		HeaderDelimitedTable delimitedTable = null;
		for (ITableHeaderDelimiter delimiter : delimiters) {
			delimitedTable = delimiter.delimit(element);
			if (isValid(delimitedTable)) {
				return delimitedTable;
			}
		}
		return delimitedTable;
	}

	private boolean isValid(HeaderDelimitedTable delimitedTable) {
		return delimitedTable.getDataTableHeaderType() != DataTableHeaderType.UnDetermined
				&& delimitedTable.getDataTableHeaderType() != DataTableHeaderType.NonHeaderTable;
	}

}
