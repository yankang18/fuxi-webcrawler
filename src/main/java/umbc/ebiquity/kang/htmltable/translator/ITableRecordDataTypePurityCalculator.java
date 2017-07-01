package umbc.ebiquity.kang.htmltable.translator;

import java.util.List;

import umbc.ebiquity.kang.htmltable.core.TableRecord;

public interface ITableRecordDataTypePurityCalculator {
	double computeDataTypePurityScore(List<TableRecord> dataRecords, int offset, double beta);
}
