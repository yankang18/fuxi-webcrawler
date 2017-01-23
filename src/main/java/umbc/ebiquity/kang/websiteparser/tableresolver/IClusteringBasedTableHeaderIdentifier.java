package umbc.ebiquity.kang.websiteparser.tableresolver;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.websiteparser.tableresolver.impl.TableResolveResult;

public interface IClusteringBasedTableHeaderIdentifier {

	TableResolveResult identify(Element element); 

}
