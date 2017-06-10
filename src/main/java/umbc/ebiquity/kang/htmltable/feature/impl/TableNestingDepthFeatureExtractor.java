package umbc.ebiquity.kang.htmltable.feature.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmltable.IHTMLDataTable;
import umbc.ebiquity.kang.htmltable.core.HTMLDataTable;
import umbc.ebiquity.kang.htmltable.core.HTMLTableTagDefinition;
import umbc.ebiquity.kang.htmltable.feature.ITableFeatureExtractor;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimiliartySuite;

public class TableNestingDepthFeatureExtractor implements ITableFeatureExtractor {

	@Override
	public String getFeatureName() {
		return "DepthOfTables";
	}

	@Override
	public Object extractFeatureValue(IHTMLDataTable dataTable, ITableRecordsSimiliartySuite similaritySuite) {
		Element element = dataTable.getWrappedElement();
		return depthOfNestedTables(element);
	}

	private int depthOfNestedTables(Element element) {
		int depthOfNestedTables = 0;
		if (HTMLTableTagDefinition.isTableTag(element.tagName())) {
			depthOfNestedTables += 1;
		}

		// Calculate depth of nested tables from each child of current element.
		int dntOfChildren = 0;
		for (Element child : element.children()) {
			int temp = depthOfNestedTables(child);
			if (temp > dntOfChildren) {
				dntOfChildren = temp;
			}
		}
		depthOfNestedTables += dntOfChildren;
		return depthOfNestedTables;
	}

}
