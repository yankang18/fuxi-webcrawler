package umbc.ebiquity.kang.htmltable.delimiter;

import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;

/**
 * This interface defines the behavior of implementing classes that separate a
 * table header records from table data records.
 * 
 * @author yankang
 *
 */
public interface ITableHeaderDelimiter {

	/**
	 * Splits a HTML table represented by {@link org.jsoup.nodes.Element} to a
	 * set of table header records and a set of table data records.
	 * 
	 * @param element
	 *            the {@link org.jsoup.nodes.Element} representing a HTML table
	 * @return a <code>TableResolveResult<code> holding resolved table
	 *         information
	 *         
	 * @throws IllegalArgumentException if the specified <code>Element</code> is
	 *             not a HTML table
	 */
	HeaderDelimitedTable delimit(Element element);

}
