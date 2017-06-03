package umbc.ebiquity.kang.webtable.delimiter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.htmldocument.impl.StandardHtmlElement;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.webtable.delimiter.impl.HeaderDelimitedTable;
import umbc.ebiquity.kang.webtable.delimiter.impl.TableHeaderLocatingResult;

public abstract class AbstractHTMLHeaderTagBasedTableHeaderDelimiter implements ITableHeaderDelimiter {

	protected HeaderDelimitedTable resolveHeaderFromBody(Element tableElem, IHeaderLocatingBorker broker) {
		Elements tbodies = tableElem.getElementsByTag("tbody");
		HeaderDelimitedTable result;
		if (tbodies.size() > 0) {
			Element tBody = tbodies.get(0);
			Elements elements = tBody.children();
			if (elements.size() > 0) {
				result = convertToTableResolveResult(tBody, broker.locateHeader(elements));
			} else {
				// no row in tbody --> not regular
				result = convertToTableResolveResult(tBody, new TableHeaderLocatingResult(TableStatus.UnRegularTable,
						DataTableHeaderType.UnDetermined, false));
			}
		} else {
			// there is no tbody
			Elements elements = tableElem.children();
			if (elements.size() > 0) {
				result = convertToTableResolveResult(tableElem, broker.locateHeader(elements));
			} else {
				// no row in table --> not regular
				result = convertToTableResolveResult(tableElem, new TableHeaderLocatingResult(
						TableStatus.UnRegularTable, DataTableHeaderType.UnDetermined, false));
			}
		}
		result.setHtmlElement(StandardHtmlElement.createDefaultStandardHtmlElement(tableElem));
		return result;
	}

	/**
	 * 
	 * @param element
	 * @param result
	 * @return
	 */
	protected abstract HeaderDelimitedTable convertToTableResolveResult(Element element, TableHeaderLocatingResult result);
	
	protected interface IHeaderLocatingBorker {
		TableHeaderLocatingResult locateHeader(Elements elements);
	}
}
