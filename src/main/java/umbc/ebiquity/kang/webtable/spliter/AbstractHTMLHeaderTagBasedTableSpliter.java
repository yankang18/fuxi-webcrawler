package umbc.ebiquity.kang.webtable.spliter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.webtable.spliter.ITableHeaderResolver.TableStatus;
import umbc.ebiquity.kang.webtable.spliter.impl.TableHeaderLocatingResult;
import umbc.ebiquity.kang.webtable.spliter.impl.TableSplitingResult;

public abstract class AbstractHTMLHeaderTagBasedTableSpliter implements ITableSpliter {

	protected TableSplitingResult resolveHeaderFromBody(Element tableElem, IHeaderLocatingBorker broker) {
		Elements tbodies = tableElem.getElementsByTag("tbody");
		if (tbodies.size() > 0) {
			Element tBody = tbodies.get(0);
			Elements elements = tBody.children();
			if (elements.size() > 0) {
				return convertToTableResolveResult(tBody, broker.locateHeader(elements));
			} else {
				// no row in tbody --> not regular
				return convertToTableResolveResult(tBody,
						new TableHeaderLocatingResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, false));
			}
		} else {
			// there is no tbody
			Elements elements = tableElem.children();
			if (elements.size() > 0) {
				return convertToTableResolveResult(tableElem, broker.locateHeader(elements));
			} else {
				// no row in table --> not regular
				return convertToTableResolveResult(tableElem,
						new TableHeaderLocatingResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, false));
			}
		}
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
