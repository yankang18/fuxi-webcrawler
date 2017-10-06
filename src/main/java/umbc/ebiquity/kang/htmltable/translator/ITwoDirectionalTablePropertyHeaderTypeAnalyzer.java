package umbc.ebiquity.kang.htmltable.translator;

import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;
import umbc.ebiquity.kang.htmltable.translator.impl.StandardTwoDirectionalTablePropertyHeaderTypeAnalyzer.TwoDirectionalHeaderType;

public interface ITwoDirectionalTablePropertyHeaderTypeAnalyzer { 

	/**
	 * @param table, the table to be analyzed and it typically is TwoDirectionalHeaderTable
	 * @return TwoDirectionalHeaderType
	 */
	TwoDirectionalHeaderType analyze(HeaderDelimitedTable table);

}
