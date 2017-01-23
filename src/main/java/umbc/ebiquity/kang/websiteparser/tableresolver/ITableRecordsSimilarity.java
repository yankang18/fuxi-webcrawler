package umbc.ebiquity.kang.websiteparser.tableresolver;

import java.util.Set;

import umbc.ebiquity.kang.websiteparser.tableresolver.impl.TableRecord;

public interface ITableRecordsSimilarity {

	double computeSimilarity(Set<TableRecord> tableRecord1, Set<TableRecord> tableRecord2); 

}
