package umbc.ebiquity.kang.webtable.similarity;

import java.util.List;

import umbc.ebiquity.kang.webtable.similarity.impl.TableRecordsSimiliartySuite;
import umbc.ebiquity.kang.webtable.spliter.impl.TableRecord;

public interface ITableRecordsSimilarityFactory {

	TableRecordsSimiliartySuite createTableRecordsSimilairtySuite(List<TableRecord> tableRecords);

}
