package umbc.ebiquity.kang.htmltable;

import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmltable.core.TableRecord;

public interface IHTMLDataTable {

	int getRowCount();

	int getColumnCount();

	List<TableRecord> getTableRecords();

	Element getWrappedElement();

}
