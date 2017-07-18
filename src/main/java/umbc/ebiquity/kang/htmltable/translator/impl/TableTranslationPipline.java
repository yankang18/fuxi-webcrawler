package umbc.ebiquity.kang.htmltable.translator.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.StandardHTMLTreeBlankNodeConsolidator;
import umbc.ebiquity.kang.htmldocument.util.HTMLTree2JSONTranslator;
import umbc.ebiquity.kang.htmldocument.util.HTMLTreeUtil;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.htmltable.delimiter.ITableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;

public class TableTranslationPipline {
	
	public void testTwoDirectionalHeaderTableHeaderTable(ITableHeaderDelimiter delimiter) throws IOException {

		// TODO: put (1)(2)(3) together from html table to html table tree
		
		// (1) Create a table element from certain source.
		Document doc = Jsoup.parse("");
		Element element = doc.getElementsByTag("table").get(0);

		// (2) table Element -- (ITableHeaderDelimiter) --> HeaderDelimitedTable
		HeaderDelimitedTable delimitedTable = delimiter.delimit(element);
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());

		// (3) HeaderDelimitedTable -- (TableTreeTranslator) --> IHTMLTreeNode
		// representing the table
		TableTreeTranslator tableTranslator = new TableTreeTranslator();
		IHTMLTreeNode tree = tableTranslator.translate(delimitedTable);

		HTMLTreeUtil.prettyPrint(tree);
		
		
		// TODO: put (4)(5)(6) together from html table tree to JSON tree

		// (4)
		IHTMLTreeOverlay overlay = HTMLTreeOverlay.createDefaultHTMLTreeOverlay(tree);

		// (5) IHTMLTreeOverlay -- (StandardHTMLTreeBlankNodeConsolidator) --> IHTMLTreeOverlay
		StandardHTMLTreeBlankNodeConsolidator cc = new StandardHTMLTreeBlankNodeConsolidator();
		overlay = cc.refine(overlay);
		HTMLTreeUtil.prettyPrint(overlay.getTreeRoot());

		// (6) IHTMLTreeOverlay -- (HTMLTree2JSONTranslator) --> JSONObject representing the table
		System.out.println("--------");
		JSONObject object = HTMLTree2JSONTranslator.translate(overlay.getTreeRoot());
		System.out.println(HTMLTree2JSONTranslator.prettyPrint(object));
	}
}
