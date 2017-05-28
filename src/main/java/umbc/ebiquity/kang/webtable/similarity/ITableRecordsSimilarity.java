package umbc.ebiquity.kang.webtable.similarity;

import java.util.Set;

import umbc.ebiquity.kang.webtable.core.TableRecord;

public interface ITableRecordsSimilarity {

	/**
	 * Compute the similarity between two sets of <code>TableRecord</code>s.
	 * 
	 * @param tableRecord1
	 *            a set of <code>TableRecord</code>s
	 * @param tableRecord2
	 *            a set of <code>TableRecord</code>s
	 * @return the similarity score
	 */
	double computeSimilarity(Set<TableRecord> tableRecord1, Set<TableRecord> tableRecord2);

}
