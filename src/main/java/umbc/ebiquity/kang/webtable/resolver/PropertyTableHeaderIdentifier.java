package umbc.ebiquity.kang.webtable.resolver;

import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeEntityNode;
import umbc.ebiquity.kang.webtable.core.TableRecord;

/**
 * This class is to identify property header records from a list of table header
 * records.
 * 
 * @author yankang
 *
 */
public interface PropertyTableHeaderIdentifier {

	/**
	 * Identify a property header record from a list of table header.
	 * 
	 * @param headerRecords
	 *            a list of header records
	 * @param skipCellNumber
	 *            the number of cells to skip from the start of each record
	 * @return a list of IHTMLTreeNode each of which represents a property
	 */
	List<HTMLTreeEntityNode> identifyPropertyHeader(List<TableRecord> headerRecords, int skipCellNumber);

}
