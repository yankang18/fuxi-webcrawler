package umbc.ebiquity.kang.htmltable.translator.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreePropertyNode;
import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.translator.IPropertyTableHeaderTranslator;

public class SimplePropertyTableHeaderTranslator implements IPropertyTableHeaderTranslator {

	@Override
	public List<HTMLTreePropertyNode> translate(List<TableRecord> headerRecords, int skipCellNumber) {
		List<HTMLTreePropertyNode> bestPropertyHeader = null;
		double max = 0;
		for (TableRecord record : headerRecords) {
			List<HTMLTreePropertyNode> propertyHeaderNodes = new ArrayList<>(headerRecords.size());
			double score = 0.0;
			List<TableCell> tableCells = record.getTableCells();
			int i = 0;
			for (; i < skipCellNumber; i++) {
				propertyHeaderNodes.add(createPropertyNode(tableCells.get(i)));
			}
			for (; i < tableCells.size(); i++) {
				propertyHeaderNodes.add(createPropertyNode(tableCells.get(i)));
				score += computeScore(extractContent(tableCells.get(i)));
			}
			if (score > max) {
				max = score;
				bestPropertyHeader = propertyHeaderNodes;
			}
		}
		return bestPropertyHeader;
	}

	private double computeScore(String content) {
		if (content != null && !content.trim().isEmpty())
			return 1;
		return 0;
	}

	private HTMLTreePropertyNode createPropertyNode(TableCell tableCell) {
		HTMLTreePropertyNode entityNode = new HTMLTreePropertyNode(tableCell.getWrappedElement(),
				extractContent(tableCell));
		return entityNode;
	}

	private String extractContent(TableCell tableCell) {
		Element element = tableCell.getWrappedElement();
		return element != null ? tableCell.getWrappedElement().text() : "";
	}

}
