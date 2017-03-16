package umbc.ebiquity.kang.webtable.similarity.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import umbc.ebiquity.kang.webtable.similarity.ITableRecordsSimilarityFactory;
import umbc.ebiquity.kang.webtable.spliter.impl.TableRecord;
import umbc.ebiquity.kang.webtable.spliter.impl.TableRecordAttributeTracer;

public class TableRecordsSimilarityFactory implements ITableRecordsSimilarityFactory {

	@Override
	public TableRecordsSimiliartySuite createTableRecordsSimilairtySuite(List<TableRecord> tableRecords) {
		Set<String> attributesToBeTraced = new HashSet<String>();
		attributesToBeTraced.add("class");
		attributesToBeTraced.add("valign");
		attributesToBeTraced.add("align");
		TableRecordAttributeTracer tableRecordAttributeTracer = new TableRecordAttributeTracer(attributesToBeTraced);
		tableRecordAttributeTracer.mark(tableRecords);

		AttributesSimilarity attributesSimilarity = new AttributesSimilarity(tableRecordAttributeTracer);
		DataCellsSimilarity dataCellsSimilarity = new DataCellsSimilarity(attributesSimilarity);
		TableRecordSimilarity tableRecordSimilarity = new TableRecordSimilarity(attributesSimilarity,
				dataCellsSimilarity);
		TableRecordsSimilarity tableRecordsSimilarity = new TableRecordsSimilarity(tableRecordSimilarity);
		TableRecordsSimiliartySuite suite = new TableRecordsSimiliartySuite(tableRecordsSimilarity,
				tableRecordSimilarity, dataCellsSimilarity, attributesSimilarity);
		return suite;
	}
}
