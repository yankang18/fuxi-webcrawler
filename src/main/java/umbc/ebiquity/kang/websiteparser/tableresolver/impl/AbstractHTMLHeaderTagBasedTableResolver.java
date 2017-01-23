package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.DataTableHeaderType;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableHeaderResolver.TableStatus;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableResolver;

public abstract class AbstractHTMLHeaderTagBasedTableResolver implements ITableResolver {

	protected TableResolveResult resolveHeaderFromBody(Element tableElem, IHeaderResolutionWorker worker) {
		Elements tbodies = tableElem.getElementsByTag("tbody");
		if (tbodies.size() > 0) {
			Element tBody = tbodies.get(0);
			Elements elements = tBody.children();
			if (elements.size() > 0) {
				return convertToTableResolveResult(tBody, worker.resolveHeader(elements));
			} else {
				// no row in tbody --> not regular
				return convertToTableResolveResult(tBody,
						new TableHeaderResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, false));
			}
		} else {
			// there is no tbody
			Elements elements = tableElem.children();
			if (elements.size() > 0) {
				return convertToTableResolveResult(tableElem, worker.resolveHeader(elements));
			} else {
				// no row in table --> not regular
				return convertToTableResolveResult(tableElem,
						new TableHeaderResolveResult(TableStatus.UnRegularTable, DataTableHeaderType.UD, false));
			}
		}
	}

	protected abstract TableResolveResult convertToTableResolveResult(Element element, TableHeaderResolveResult result);
	
	protected interface IHeaderResolutionWorker {
		TableHeaderResolveResult resolveHeader(Elements elements);
	}
}
