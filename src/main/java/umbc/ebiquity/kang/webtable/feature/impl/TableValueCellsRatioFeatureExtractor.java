package umbc.ebiquity.kang.webtable.feature.impl;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import umbc.ebiquity.kang.textprocessing.util.TextProcessingUtils;
import umbc.ebiquity.kang.websiteparser.impl.HTMLTags;
import umbc.ebiquity.kang.webtable.IHTMLDataTable;
import umbc.ebiquity.kang.webtable.feature.ITableFeatureExtractor;
import umbc.ebiquity.kang.webtable.similarity.ITableRecordsSimiliartySuite;
import umbc.ebiquity.kang.webtable.spliter.impl.TableCell;
import umbc.ebiquity.kang.webtable.spliter.impl.TableRecord;

public class TableValueCellsRatioFeatureExtractor implements ITableFeatureExtractor {

	@Override
	public String getFeatureName() {
		return "RatioOfValueCells";
	}

	@Override
	public Object extractFeatureValue(IHTMLDataTable dataTable, ITableRecordsSimiliartySuite similaritySuite) {
		return ratioOfValueCells(dataTable);
	}

	private double ratioOfValueCells(IHTMLDataTable dataTable) {
		double numOfValues = 0;
		double cellCount = 0;
		for (TableRecord record : dataTable.getTableRecords()) {
			for (TableCell cell : record.getTableCells()) {
				Element tcElement = cell.getWrappedElement();
				numOfValues += isValueCell(tcElement);
				cellCount++;
			}
		}
		return cellCount == 0 ? 0.0 : numOfValues / cellCount;
	}

	private int isValueCell(Element tcElement) {

		if (hasNoValidChildElements(tcElement)) {
			if (notEmpty(tcElement.text())) {
				return 1;
			} else {
				return 0;
			}
		}

		for (Element elem : tcElement.children()) {
			String tagName = elem.tagName();
			if (HTMLTags.isImageTag(tagName) || (!HTMLTags.isTopicTag(tagName) && !HTMLTags.isValueTag(tagName)
					&& !HTMLTags.isLineBreaker(tagName))) {
				return 0;
			}
		}

		// all child elements are topic, value (except image) or line breaker
		if (notEmpty(tcElement.text())) {
			return 1;
		} else {
			return 0;
		}
	}

	private boolean hasNoValidChildElements(Element tcElement) {
		Elements childElements = tcElement.children();

		// This element has no child elements
		if (childElements.size() == 0)
			return true;

		// br element does not count as child elements. Therefore, if all child
		// elements are br, we consider this element has no child elements.
		for (Element c : childElements) {
			if (!"br".equalsIgnoreCase(c.tagName().trim())) {
				return false;
			}
		}
		return true;
	}

	private boolean notEmpty(String text) {
		if (TextProcessingUtils.isStringEmpty(text)) {
			return false;
		}
		return true;
	}

}
