package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import java.util.List;
import java.util.Set;

import umbc.ebiquity.kang.websiteparser.tableresolver.IAttributesSimilarity;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableRecordSimilarity;

public class TableRecordSimilarity implements ITableRecordSimilarity {

	private IAttributesSimilarity attributesSimilarity;
	private double weight1 = 0.4;
	private double weight2 = 0.3;
	private double weight3 = 0.3;
	private boolean hasTableRecordLevelTracedAttrs;
	private boolean hasTableCellLevelTracedAttrs;

	TableRecordSimilarity(IAttributesSimilarity attributesSimilarity) {
		this.attributesSimilarity = attributesSimilarity;
	}

	@Override
	public double computeSimilarity(TableRecord record1, TableRecord record2) {

		hasTableRecordLevelTracedAttrs = attributesSimilarity.hasTracedAttributes(record1);
		hasTableCellLevelTracedAttrs = attributesSimilarity.hasTracedAttributes(record1.getTableCells().get(0));
		
		adjustWeights();
		
		double trAttrsSim = 0.0;
		if(hasTableRecordLevelTracedAttrs){
			 trAttrsSim = attributesSimilarity.computeSimilarity(record1, record2);
		}

		List<TableCell> tableCellsList1 = record1.getTableCells();
		List<TableCell> tableCellsList2 = record2.getTableCells();

		double aveTcAttrsSim = 0.0;
		if (hasTableCellLevelTracedAttrs) {
			double sum = 0.0;
			int count = tableCellsList1.size();
			for (int i = 0; i < count; i++) {
				sum += attributesSimilarity.computeSimilarity(tableCellsList1.get(i), tableCellsList2.get(i));
			}
			aveTcAttrsSim = count == 0 ? sum : sum / count;
		}

		double sum2 = 0.0;
		int count = tableCellsList1.size();
		for (int i = 0; i < count; i++) {
			sum2 += computeDataCellsSimilarity(tableCellsList1.get(i), tableCellsList2.get(i));
		}

		double aveDcSim = count == 0 ? sum2 : sum2 / count;

		return weight1 * trAttrsSim + weight2 * aveTcAttrsSim + weight3 * aveDcSim;
	}
	
	private void adjustWeights() {
		if (hasTableRecordLevelTracedAttrs && hasTableCellLevelTracedAttrs) {
			weight1 = 0.4;
			weight2 = 0.3;
			weight3 = 0.3;
		} else if (hasTableRecordLevelTracedAttrs && !hasTableCellLevelTracedAttrs) {
			weight1 = 0.5;
			weight2 = 0.0;
			weight3 = 0.5;
		} else if (!hasTableRecordLevelTracedAttrs && hasTableCellLevelTracedAttrs) {
			weight1 = 0.0;
			weight2 = 0.5;
			weight3 = 0.5;
		} else {
			weight1 = 0.0;
			weight2 = 0.0;
			weight3 = 1.0;
		}
	}
	
	private double computeDataCellsSimilarity(TableCell tableCell1, TableCell tableCell2) {
		Set<String> keySet1 = tableCell1.getDataCellKeySet();
		Set<String> keySet2 = tableCell2.getDataCellKeySet();
		double sim1 = computeDataCellsSimBasedOnKeySet(tableCell1, tableCell2, keySet1);
		double sim2 = computeDataCellsSimBasedOnKeySet(tableCell1, tableCell2, keySet2);
		return Math.min(sim1, sim2);
	}

	private double computeDataCellsSimBasedOnKeySet(TableCell tableCell1, TableCell tableCell2, Set<String> keySet) {
		
		double sum = 0.0;
		int count = keySet.size();
		for (String key : keySet) {
			DataCell dataCell1 = tableCell1.getDataCell(key);
			DataCell dataCell2 = tableCell2.getDataCell(key);
			if (dataCell1 != null && dataCell2 != null) {
				sum += computeSimilairty(dataCell1, dataCell2);
			}
		}

		if (count == 0)
			return sum;
		else
			return sum / count;
	}

	private double computeSimilairty(DataCell dataCell1, DataCell dataCell2) {
		int count = 1;
		double sim = computeStringSimilarity(dataCell1.getTagName(), dataCell1.getTagName());
		if (attributesSimilarity.hasTracedAttributes(dataCell1) && attributesSimilarity.hasTracedAttributes(dataCell2)) {
			count++;
			sim += attributesSimilarity.computeSimilarity(dataCell1, dataCell2);
		}
		return sim / count;
	}


	private double computeStringSimilarity(String string1, String string2) {
		return string1.equals(string2) ? 1.0 : 0.0;
	}

}
