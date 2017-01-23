package umbc.ebiquity.kang.websiteparser.tableresolver;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.websiteparser.tableresolver.impl.TableResolveResult;

public interface ITableResolver {

	TableResolveResult resolve(Element element); 

}
