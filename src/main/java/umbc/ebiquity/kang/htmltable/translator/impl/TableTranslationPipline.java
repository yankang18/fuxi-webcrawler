package umbc.ebiquity.kang.htmltable.translator.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeNode;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.IHTMLTreeOverlayRefiner;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.HTMLTreeOverlay;
import umbc.ebiquity.kang.htmldocument.parser.htmltree.impl.StandardHTMLTreeBlankNodeConsolidator;
import umbc.ebiquity.kang.htmldocument.util.HTMLTree2JSONConverter;
import umbc.ebiquity.kang.htmldocument.util.HTMLTreeUtil;
import umbc.ebiquity.kang.htmltable.delimiter.IDelimitedTable.TableStatus;
import umbc.ebiquity.kang.htmltable.delimiter.ITableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.delimiter.impl.HeaderDelimitedTable;
import umbc.ebiquity.kang.htmltable.delimiter.impl.StandardTableHeaderDelimiter;
import umbc.ebiquity.kang.htmltable.translator.ITableTreeTranslator;

public class TableTranslationPipline {
	
	private ITableHeaderDelimiter headerDelimiter;
	private ITableTreeTranslator tableTreeTranslator;
	private IHTMLTreeOverlayRefiner htmlTreeBlankNodeConsolidator;

	private TableTranslationPipline(TableTranslationPiplineBuilder piplineBuilder) {
		this.headerDelimiter = piplineBuilder.headerDelimiter;
		this.tableTreeTranslator = piplineBuilder.tableTreeTranslator;
		this.htmlTreeBlankNodeConsolidator = piplineBuilder.htmlTreeBlankNodeConsolidator;
	}
	
	public JSONObject run() throws IOException {

		/*
		 * (1)(2)(3) translate a html table to a html table tree
		 */

		// (1) Create a table element from certain source.
		Document doc = Jsoup.parse("");
		Element element = doc.getElementsByTag("table").get(0);

		// (2) table Element -- (ITableHeaderDelimiter) --> HeaderDelimitedTable
		HeaderDelimitedTable delimitedTable = headerDelimiter.delimit(element);
		assertEquals(TableStatus.RegularTable, delimitedTable.getTableStatus());

		// (3) HeaderDelimitedTable -- (TableTreeTranslator) --> IHTMLTreeNode
		// representing the table
		IHTMLTreeNode tree = tableTreeTranslator.translate(delimitedTable);

		HTMLTreeUtil.prettyPrint(tree);
		
		
		/*
		 * (4)(5)(6) translate a html table tree to a JSON tree
		 */

		// (4)
		IHTMLTreeOverlay overlay = HTMLTreeOverlay.createDefaultHTMLTreeOverlay(tree);

		// (5) IHTMLTreeOverlay -- (StandardHTMLTreeBlankNodeConsolidator) --> IHTMLTreeOverlay
		overlay = htmlTreeBlankNodeConsolidator.refine(overlay);
		
		HTMLTreeUtil.prettyPrint(overlay.getTreeRoot());

		// (6) IHTMLTreeOverlay -- (HTMLTree2JSONTranslator) --> JSONObject representing the table
		JSONObject object = HTMLTree2JSONConverter.convert(overlay.getTreeRoot());
		
		System.out.println(HTMLTree2JSONConverter.prettyPrint(object));
		
		return object;
	}
	
	
	public static class TableTranslationPiplineBuilder {

		private ITableHeaderDelimiter headerDelimiter;
		private ITableTreeTranslator tableTreeTranslator;
		private IHTMLTreeOverlayRefiner htmlTreeBlankNodeConsolidator;

		public TableTranslationPiplineBuilder() {
			headerDelimiter = new StandardTableHeaderDelimiter();
			tableTreeTranslator = new TableTreeTranslator();
			htmlTreeBlankNodeConsolidator = new StandardHTMLTreeBlankNodeConsolidator();
		}

		public TableTranslationPiplineBuilder setTableHeaderDelimiter(ITableHeaderDelimiter headerDelimiter) {
			this.headerDelimiter = headerDelimiter;
			return this;
		}

		public TableTranslationPiplineBuilder setTableTreeTranslator(ITableTreeTranslator tableTreeTranslator) {
			this.tableTreeTranslator = tableTreeTranslator;
			return this;
		}

		public TableTranslationPiplineBuilder setHTMLTreeBlankNodeConsolidator(
				IHTMLTreeOverlayRefiner htmlTreeBlankNodeConsolidator) {
			this.htmlTreeBlankNodeConsolidator = htmlTreeBlankNodeConsolidator;
			return this;
		}

		public TableTranslationPipline build() {
			return new TableTranslationPipline(this);
		}
	}
}
