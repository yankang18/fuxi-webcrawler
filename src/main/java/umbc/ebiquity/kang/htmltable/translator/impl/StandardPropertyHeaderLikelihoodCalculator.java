package umbc.ebiquity.kang.htmltable.translator.impl;

import java.util.List;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.translator.IPropertyHeaderLikelihoodCalculator;

public class StandardPropertyHeaderLikelihoodCalculator implements IPropertyHeaderLikelihoodCalculator {

	@Override
	public double calculate(List<TableRecord> headerRecords, int offset) {
		double max = 0;
		for (TableRecord record : headerRecords) {
			double totalSim = 0.0;
			int count = 0;
			List<TableCell> tableCells = record.getTableCells();
			for (int i = offset; i < tableCells.size(); i++) {
				// TODO: may also consider checking data type or unit
				// information in each property record cell
				totalSim += DictionaryBasedPropertyScore.calculate(extractContent(tableCells.get(i)));
				count++;
			}
			max = Math.max(max, totalSim / count);
		}
		return max;
	}

	private String extractContent(TableCell tableCell) {
		Element element = tableCell.getWrappedElement();
		return element != null ? tableCell.getWrappedElement().text() : "";
	}

}
