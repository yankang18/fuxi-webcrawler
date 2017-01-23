package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import java.util.Set;

import umbc.ebiquity.kang.websiteparser.tableresolver.ITableRecordSimilarity;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableRecordsSimilarity;

public class TableRecordsSimilarity implements ITableRecordsSimilarity {
	
	private ITableRecordSimilarity tableRecordSimilarity;

	TableRecordsSimilarity(ITableRecordSimilarity tableRecordSimilarity) {
		this.tableRecordSimilarity = tableRecordSimilarity;
	}
	
	@Override
	public double computeSimilarity(Set<TableRecord> tableRecord1, Set<TableRecord> tableRecord2){
		double sim1 = computeDirectionalSimilarity(tableRecord1, tableRecord2);
		double sim2 = computeDirectionalSimilarity(tableRecord2, tableRecord1);
		return (sim1 + sim2) / 2;
	}
	
	private double computeDirectionalSimilarity(Set<TableRecord> memberSet1, Set<TableRecord> memberSet2) {
		double totalSim = 0.0;
		for (TableRecord member1 : memberSet1) {
			double sim = 0.0;
			for (TableRecord member2 : memberSet2) {
				sim += tableRecordSimilarity.computeSimilarity(member1, member2);
			}
			totalSim += sim / memberSet2.size();
		}
		return totalSim / memberSet1.size();
	}

}
