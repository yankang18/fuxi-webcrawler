package umbc.ebiquity.kang.htmltable.translator;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;

public interface ITableTreeTranslator {

	IHTMLTreeNode translate(HeaderDelimitedTable headerDelimitedTable); 

}
