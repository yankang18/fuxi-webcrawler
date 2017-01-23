package umbc.ebiquity.kang.websiteparser.tableresolver;

import java.util.List;

import umbc.ebiquity.kang.websiteparser.tableresolver.impl.TableRecord;

public interface ITableRecordsSimilarityFactory {

	ITableRecordsSimilarity createTableRecordsSimilairty(List<TableRecord> tableRecords); 

}
