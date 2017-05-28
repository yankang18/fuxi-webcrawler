package umbc.ebiquity.kang.webtable.similarity;

import java.util.List;

import umbc.ebiquity.kang.webtable.core.TableRecord;
import umbc.ebiquity.kang.webtable.similarity.impl.TableRecordsSimiliartySuite;

public interface ITableRecordsSimilarityFactory {

	TableRecordsSimiliartySuite createTableRecordsSimilairtySuite(List<TableRecord> tableRecords);

}
