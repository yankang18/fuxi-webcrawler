package umbc.ebiquity.kang.websiteparser.tableresolver.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import umbc.ebiquity.kang.websiteparser.tableresolver.ITableRecordsSimilarity;
import umbc.ebiquity.kang.websiteparser.tableresolver.ITableRecordsSimilarityFactory;

public class TableRecordsSimilarityFactory implements ITableRecordsSimilarityFactory{

	@Override
	public ITableRecordsSimilarity createTableRecordsSimilairty(List<TableRecord> tableRecords) {
		TableRecordSimilarity tableRecordSimilarity = createTableRecordSimilarity(tableRecords);
		return new TableRecordsSimilarity(tableRecordSimilarity);
	}

	private TableRecordSimilarity createTableRecordSimilarity(List<TableRecord> tableRecords) {
		Set<String> attributesToBeTraced = new HashSet<String>();
		attributesToBeTraced.add("class");
		TableRecordAttributeTracer tableRecordAttributeTracer = new TableRecordAttributeTracer(attributesToBeTraced);
		tableRecordAttributeTracer.mark(tableRecords);
		AttributesSimilarity attributesSimilarity = new AttributesSimilarity(tableRecordAttributeTracer);
		TableRecordSimilarity tableRecordSimilarity = new TableRecordSimilarity(attributesSimilarity);
		return tableRecordSimilarity;
	}
}
