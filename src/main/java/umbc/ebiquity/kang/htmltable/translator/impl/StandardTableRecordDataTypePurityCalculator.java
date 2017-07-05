package umbc.ebiquity.kang.htmltable.translator.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IValueTypeResolver;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.ValueType;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.ValueTypeInfo;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.nlp.StandardValueTypeResolver;
import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.translator.ITableRecordDataTypePurityCalculator;
import umbc.ebiquity.kang.machinelearning.math.util.BasicMath;

public class StandardTableRecordDataTypePurityCalculator implements ITableRecordDataTypePurityCalculator {

	private IValueTypeResolver valueTypeResolver = new StandardValueTypeResolver();
	
	@Override
	public double computeDataTypePurityScore(List<TableRecord> dataRecords, int offset, double beta) {

		// Currently, we are not using beta parameter
		beta = beta > 0 ? BasicMath.roundDown(beta, 2) : 0;
		double sum = 0.0;
		for (TableRecord record : dataRecords) {
			Map<String, Integer> map = new HashMap<>();
			List<TableCell> tableCells = record.getTableCells();
			int cellCount = tableCells.size() - offset;
			for (int i = offset; i < tableCells.size(); i++) {
				ValueTypeInfo valueTypeInfo = valueTypeResolver.resolve(extractContent(tableCells.get(i)));
				ValueType valueType = valueTypeInfo.getValueType();
				String group = valueType.name();
				if (ValueType.NumberPhrase == valueType) {
					group = valueTypeInfo.getUnit();
				}

				Integer frequency = map.get(group);
				if (frequency == null) {
					map.put(group, 1);
				} else {
					map.put(group, frequency + 1);
				}
			}
			double normalizedEntropy = BasicMath.computeEntropy(map) / BasicMath.computeMaxEntropy(cellCount);
			if(beta > 0) {
				normalizedEntropy = Math.pow(normalizedEntropy, 1 / beta);
			} 
			sum += normalizedEntropy;
		}
		return 1 - sum / dataRecords.size();
	}
	
	private String extractContent(TableCell tableCell) {
		Element element = tableCell.getWrappedElement();
		return element != null ? tableCell.getWrappedElement().text() : "";
	}

}
