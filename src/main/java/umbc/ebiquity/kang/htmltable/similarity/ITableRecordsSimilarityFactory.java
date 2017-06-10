package umbc.ebiquity.kang.htmltable.similarity;

import java.util.List;

import umbc.ebiquity.kang.htmltable.core.TableRecord;
import umbc.ebiquity.kang.htmltable.similarity.impl.TableRecordsSimiliartySuite;

public interface ITableRecordsSimilarityFactory {

	TableRecordsSimiliartySuite createTableRecordsSimilairtySuite(List<TableRecord> tableRecords);

}
