package umbc.ebiquity.kang.htmltable.translator;

import java.util.List;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreePropertyNode;
import umbc.ebiquity.kang.htmltable.core.TableRecord;

/**
 * This class is to identify property header records from a list of table header
 * records.
 * 
 * @author yankang
 *
 */
public interface IPropertyHeaderTranslator {

	/**
	 * Translates a list of TableRecord representing property table header to a
	 * list HTMLTreeEntityNode representing property table header.
	 * 
	 * @param headerRecords
	 *            a list of header records
	 * @param skipCellNumber
	 *            the number of cells to skip from the start of each record
	 * @return a list of IHTMLTreeNode each of which represents a property
	 */
	List<HTMLTreePropertyNode> translate(List<TableRecord> headerRecords, int skipCellNumber);

}
