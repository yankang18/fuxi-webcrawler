package umbc.ebiquity.kang.websiteparser.tableresolver;

import umbc.ebiquity.kang.websiteparser.tableresolver.impl.TableRecord;

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
