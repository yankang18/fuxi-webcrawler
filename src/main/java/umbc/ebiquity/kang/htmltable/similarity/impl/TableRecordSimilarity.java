package umbc.ebiquity.kang.htmltable.similarity.impl;

import java.util.List;

import umbc.ebiquity.kang.htmltable.core.TableCell;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.similarity.IAttributesSimilarity;
import umbc.ebiquity.kang.htmltable.similarity.IDataCellsSimilarity;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordSimilarity;

public class TableRecordSimilarity implements ITableRecordSimilarity {

	private IAttributesSimilarity attributesSimilarity;
	private IDataCellsSimilarity dataCellsSimilarity;
	private double weight1 = 0.4;
	private double weight2 = 0.3;
	private double weight3 = 0.3;
	private boolean hasTableRecordLevelTracedAttrs;
	private boolean hasTableCellLevelTracedAttrs;

	TableRecordSimilarity(IAttributesSimilarity attributesSimilarity, IDataCellsSimilarity dataCellsSimilarity) {
		this.attributesSimilarity = attributesSimilarity;
		this.dataCellsSimilarity = dataCellsSimilarity;
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
			sum2 += dataCellsSimilarity.computeSimilarity(tableCellsList1.get(i), tableCellsList2.get(i));
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

	public IAttributesSimilarity getAttributesSimilarity() {
		return attributesSimilarity;
	}

	public IDataCellsSimilarity getDataCellsSimilarity() {
		return dataCellsSimilarity;
	}

}
