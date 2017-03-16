package umbc.ebiquity.kang.webtable.similarity.impl;

import java.util.Set;

import umbc.ebiquity.kang.webtable.IDataCellHolder;
import umbc.ebiquity.kang.webtable.similarity.IAttributesSimilarity;
import umbc.ebiquity.kang.webtable.similarity.IDataCellsSimilarity;
import umbc.ebiquity.kang.webtable.spliter.impl.DataCell;

public class DataCellsSimilarity implements IDataCellsSimilarity {
	
	private IAttributesSimilarity attributesSimilarity;

	DataCellsSimilarity(IAttributesSimilarity attributesSimilarity) {
		this.attributesSimilarity = attributesSimilarity;
	}

	@Override
	public double computeSimilarity(IDataCellHolder dataCellHolder1, IDataCellHolder dataCellHolder2) {
		Set<String> keySet1 = dataCellHolder1.getDataCellKeySet();
		Set<String> keySet2 = dataCellHolder2.getDataCellKeySet();
		double sim1 = computeDataCellsSimBasedOnKeySet(dataCellHolder1, dataCellHolder2, keySet1);
		double sim2 = computeDataCellsSimBasedOnKeySet(dataCellHolder1, dataCellHolder2, keySet2);
		return Math.min(sim1, sim2);
	}

	private double computeDataCellsSimBasedOnKeySet(IDataCellHolder tableCell1, IDataCellHolder tableCell2, Set<String> keySet) {
		
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
