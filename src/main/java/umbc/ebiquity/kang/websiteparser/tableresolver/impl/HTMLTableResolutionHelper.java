package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableTagDefinition.TABLE_DATA_TAG;
import static umbc.ebiquity.kang.websiteparser.tableresolver.impl.HTMLTableTagDefinition.TABLE_HEADER_TAG;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLTableResolutionHelper {
	
	public static int getMinVerticalColumnCount(Element element) {
		Elements elements = element.children();
		return getMinVerticalColumnCount(elements);
	}

	public static int getMinVerticalColumnCount(Elements elements) {
		int minVerticalColCount = Integer.MAX_VALUE;
		for (Element e : elements) {
			int count = 0;
			for (Element c : e.children()) {
				if (TABLE_DATA_TAG.equals(c.tagName()) || TABLE_HEADER_TAG.equals(c.tagName())) {
					count++;
				}
			}
			if (count < minVerticalColCount) {
				minVerticalColCount = count;
			}
		}
		return minVerticalColCount;
	}
	
	public static int getMaxVerticalColumnCount(Element element) {
		Elements elements = element.children();
		return getMaxVerticalColumnCount(elements);
	}

	public static int getMaxVerticalColumnCount(Elements elements) {
		int maxVerticalColCount = 0;
		for (Element e : elements) {
			int count = 0;
			for (Element c : e.children()) {
				if (TABLE_DATA_TAG.equals(c.tagName()) || TABLE_HEADER_TAG.equals(c.tagName())) {
					count++;
				}
			}
			if (count > maxVerticalColCount) {
				maxVerticalColCount = count;
			}
		}
		return maxVerticalColCount;
	}
}
