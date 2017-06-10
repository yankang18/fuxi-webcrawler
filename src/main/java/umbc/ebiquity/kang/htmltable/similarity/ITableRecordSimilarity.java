package umbc.ebiquity.kang.htmltable.similarity;

import umbc.ebiquity.kang.htmltable.core.TableRecord;

/***
 * This interface defines the behaviors of class computing the similairty
 * between two table records.
 * 
 * @author yankang
 *
 */
public interface ITableRecordSimilarity {

	double computeSimilarity(TableRecord record1, TableRecord record2);

}
