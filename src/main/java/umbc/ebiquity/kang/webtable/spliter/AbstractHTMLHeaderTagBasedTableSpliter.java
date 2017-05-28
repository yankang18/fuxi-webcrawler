package umbc.ebiquity.kang.webtable.spliter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.htmldocument.impl.StandardHtmlElement;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;
import umbc.ebiquity.kang.webtable.spliter.impl.TableHeaderLocatingResult;
import umbc.ebiquity.kang.webtable.spliter.impl.TableSplitingResult;

public abstract class AbstractHTMLHeaderTagBasedTableSpliter implements ITableHeaderSpliter {

	protected TableSplitingResult resolveHeaderFromBody(Element tableElem, IHeaderLocatingBorker broker) {
		Elements tbodies = tableElem.getElementsByTag("tbody");
		TableSplitingResult result;
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
	protected abstract TableSplitingResult convertToTableResolveResult(Element element, TableHeaderLocatingResult result);
	
	protected interface IHeaderLocatingBorker {
		TableHeaderLocatingResult locateHeader(Elements elements);
	}
}
