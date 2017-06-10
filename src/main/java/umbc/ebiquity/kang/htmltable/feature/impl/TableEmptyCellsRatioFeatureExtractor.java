package umbc.ebiquity.kang.htmltable.feature.impl;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmltable.IHTMLDataTable;
import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.feature.ITableFeatureExtractor;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimiliartySuite;
import umbc.ebiquity.kang.textprocessing.util.TextProcessingUtils;
import umbc.ebiquity.kang.websiteparser.impl.HTMLTags;

public class TableEmptyCellsRatioFeatureExtractor implements ITableFeatureExtractor {

	@Override
	public String getFeatureName() {
		return "RatioOfEmptyCells";
	}

	@Override
	public Object extractFeatureValue(IHTMLDataTable dataTable, ITableRecordsSimiliartySuite similaritySuite) {
		return ratioOfEmptyCells(dataTable);
	}

	private double ratioOfEmptyCells(IHTMLDataTable dataTable) {
		double numOfEmptyCells = 0;
		double cellCount = 0;
		for (TableRecord record : dataTable.getTableRecords()) {
			for (TableCell cell : record.getTableCells()) {
				Element tcElement = cell.getWrappedElement();
				if (isEmptyElement(tcElement)) {
					numOfEmptyCells += 1;
				}
				cellCount++;
			}
		}
		return cellCount == 0 ? 0.0 : numOfEmptyCells / cellCount;
	}

	private boolean isEmptyElement(Element tcElement) {
		if (tcElement.children().size() == 0) {
			// If this element has no child elements and contains no text, we
			// consider this element as empty.
			if (notEmpty(tcElement.text())) {
				return false;
			} else {
				return true;
			}
		} else {
			// All of the child elements are image, we consider it this element
			// as empty. Why???
			for (Element child : tcElement.children()) {
				if (!HTMLTags.isImageTag(child.tagName())) {
					return false;
				}
			}
			return true;
		}
	}

	private boolean notEmpty(String text) {
		if (TextProcessingUtils.isStringEmpty(text)) {
			return false;
		}
		return true;
	}

}
