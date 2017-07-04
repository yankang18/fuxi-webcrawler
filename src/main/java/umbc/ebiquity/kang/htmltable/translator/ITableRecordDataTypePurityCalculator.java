package umbc.ebiquity.kang.htmltable.translator;

import java.util.List;

import umbc.ebiquity.kang.htmltable.core.TableRecord;

public interface ITableRecordDataTypePurityCalculator {

	/**
	 * The smaller the score is, the more purity the data type of entries in the
	 * data records is.
	 * 
	 * @param dataRecords
	 * @param offset
	 * @param beta
	 *            if beta > 1 penalize purity; if beta < 1 boost purity; if beta
	 *            = 1, no effect
	 * @return
	 */
	double computeDataTypePurityScore(List<TableRecord> dataRecords, int offset, double beta);
}
