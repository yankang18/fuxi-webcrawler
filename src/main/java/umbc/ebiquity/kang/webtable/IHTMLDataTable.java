package umbc.ebiquity.kang.webtable;

import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.webtable.core.TableRecord;

public interface IHTMLDataTable {

	int getRowCount();

	int getColumnCount();

	List<TableRecord> getTableRecords();

	Element getWrappedElement();

}
