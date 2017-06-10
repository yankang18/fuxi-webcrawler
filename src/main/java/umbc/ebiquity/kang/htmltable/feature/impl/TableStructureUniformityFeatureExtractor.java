package umbc.ebiquity.kang.htmltable.feature.impl;

import java.util.List;

import umbc.ebiquity.kang.htmltable.IHTMLDataTable;
import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.feature.ITableFeatureExtractor;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordSimilarity;
import umbc.ebiquity.kang.htmltable.similarity.ITableRecordsSimiliartySuite;

public abstract class TableStructureUniformityFeatureExtractor implements ITableFeatureExtractor {

	@Override
	public Object extractFeatureValue(IHTMLDataTable dataTable, ITableRecordsSimiliartySuite similaritySuite) {
		return structureUniformity(dataTable, similaritySuite.getTableRecordSimilarity());
	}
	
	private double structureUniformity(IHTMLDataTable dataTable, ITableRecordSimilarity similarity) {
		List<TableRecord> tableRecords = dataTable.getTableRecords();
		int compareCount = 0;
		int size = tableRecords.size();
		double sum = 0;
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				compareCount++;
				sum += similarity.computeSimilarity(tableRecords.get(i), tableRecords.get(j));
			}
		}
		return compareCount == 0 ? 0 : sum / compareCount;
	}
}
