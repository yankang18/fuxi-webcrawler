package umbc.ebiquity.kang.htmltable.translator.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IValueTypeResolver;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeNodeValue.ValueType;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.StandardValueTypeResolver;
import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.translator.ITableRecordDataTypePurityCalculator;
import umbc.ebiquity.kang.machinelearning.math.util.Mathematics;

public class StandardTableRecordDataTypePurityCalculator implements ITableRecordDataTypePurityCalculator {

	private IValueTypeResolver valueTypeResolver = new StandardValueTypeResolver();
	
	@Override
	/**
	 * The smaller the score is, the more purity the data type of entries in the
	 * data records is.
	 * 
	 * @param dataRecords
	 * @param offset
	 * @return
	 */
	public double computeDataTypePurityScore(List<TableRecord> dataRecords, int offset, double beta) {

		// Currently, we are not using beta parameter
		double sum = 0.0;
		for (TableRecord record : dataRecords) {
			Map<String, Integer> map = new HashMap<>();
			List<TableCell> tableCells = record.getTableCells();
			int cellCount = tableCells.size() - offset;
			for (int i = offset; i < tableCells.size(); i++) {
				ValueType valueType = valueTypeResolver.resolve(extractContent(tableCells.get(i)));
				String group = valueType.name();
				if (ValueType.NumberPhrase == valueType) {
					group = valueType.getUnit();
				}

				Integer frequency = map.get(group);
				if (frequency == null) {
					map.put(group, 1);
				} else {
					map.put(group, frequency + 1);
				}
			}
			double normalizedEntropy = Mathematics.computeEntropy(map) / Mathematics.computeMaxEntropy(cellCount);
			sum += normalizedEntropy;
		}
		return 1 - sum / dataRecords.size();
	}
	
	private String extractContent(TableCell tableCell) {
		Element element = tableCell.getWrappedElement();
		return element != null ? tableCell.getWrappedElement().text() : "";
	}

}
